(ns job-board.exzigo-cache
  (:require [clj-http.client :as http]
            [slingshot.slingshot :refer [try+ throw+]]
            [cheshire.core :refer [encode generate-string parse-string]]
            [clojure.string :refer [join]]
            [clojure.walk :refer [keywordize-keys]]
            [job-board.database :as m]
            [korma.db :refer [transaction]]
            [korma.core :as db]))

;; Exzigo Endpoint
(def endpoint "http://production01.exzigo.com/service/1")

;; Path helper
(defn endpoint-url [path]
  (str endpoint path))

(defn starred [str]
  (join "" (repeat (.length str) "*")))

(defn row-exists? [table row]
  (first
   (db/select
    table
    (db/where {:id (:id row)}))))

(defn needs-update? [table row]
  (first
   (db/select
    table
    (db/where row))))

(defn update-row! [table row]
  (if (needs-update? table row)
    (db/update
     table
     (db/set-fields (dissoc row :id))
     (db/where {:id (:id row)}))))

(defn insert-row! [table row]
  (db/insert
   table
   (db/values row)))

(defn insert-or-update-in! [table row]
  (if (row-exists? table row)
    (update-row! table row)
    (insert-row! table row)))

(defn remove-difference!
  "Remove difference from table, and from assignments table if necessary.

  Assignment key is the key to determine deletion, should be a keyword."
  [table rows assignment-key]
  (let [keep-ids (map :id rows)]
    (transaction
     (db/delete
      m/assignments
      (db/where (not {assignment-key [in keep-ids]})))
     (db/delete
      table
      (db/where (not {:id [in keep-ids]}))))))

(defn merge-data-into-table!
  "Copy data into our database, optionally delete any that don't match.
   Note that this will also delete any assignments also.

  Delete must be a "
  [table data & delete?]
  (println "Cache merging in data..")
  (if (and delete? (map? delete?))
    (do
      (println "Warning! Removing difference from table.")
      (remove-difference! table data (:assignment-key delete?))))
  (doall (for [d data]
           (do
             (println "Processing" d)
             (insert-or-update-in! table d)
               d))))

(defn body-data [request]
  (parse-string (:body request)))

(defmacro defrequest [name args & reqfunc]
  `(defn ~name ~args
     (try+
      (body-data (do ~@reqfunc))
      (catch [:status 401] {:keys [status url]}
        (throw+ {:type ::authentication
                 :message "Not authenticated to perform request"
                 :method (str ~name)})))))

(defmacro request [fun token url & data]
  (if (nil? data) `(~fun (endpoint-url ~url)
                         {:query-params {"token" ~token}})
      `(fun url {:query-params {"token" token}
                :body (encode data)
                :content-type :json})))

(defrequest api-authenticate! [email password]
  (http/post (endpoint-url "/auth/user")
             {:body (encode {:email email
                             :password password})
              :content-type :json}))

(defn get-token [email password]
  (try+
   (let [new-token (get (api-authenticate! email password) "token")]
     new-token)
   (catch [:status 401] {:keys [status]}
     (throw+ {:type ::authentication :message "Could not login"
              :credentials {:email email :password (starred password)}}))))

(defrequest get-current-company [token]
  (request http/get token "/resource/companies"))

(defn current-company-id [token]
  (get (first (get-current-company token)) "company_id"))

(defrequest get-employees [token]
  (http/get (endpoint-url "/resource/employees")
            {:query-params {"token" token
                            "company_id" (current-company-id token)}}))

(defrequest get-jobsites [token]
  (http/get (endpoint-url "/resource/job_sites")
            {:query-params {"token" token
                            "company_id" (current-company-id token)}}))

(defn refresh-employees [token]
  (let [fetched (vec (keywordize-keys (get-employees token)))
        sterile (map #(select-keys % [:employee_id :first_name :last_name]) fetched)
        data (map #(dissoc (assoc % :id (:employee_id %)) :employee_id) sterile)]
    (println "I have" (count data) "employees to be merged.")
    (merge-data-into-table! m/employees data)))

(defn refresh-jobsites
  "Grabs jobsites and sticks them in the database. Does *not* fetch
  the address at the moment."
  [token]
  (let [fetched (vec (keywordize-keys (get-jobsites token)))
        sterile (map #(select-keys % [:job_site_id :name]) fetched)
        data (map #(dissoc (assoc % :id (:job_site_id %)) :job_site_id) sterile)]
    (println "I have" (count data) "jobsites to be merged.")
    (merge-data-into-table! m/jobsites data)))

(defn cache-agent-poller
  "Caching agent that pulls in jobsites and employees

  Should be called as a Future"
  [cacheagent]
  (loop [config cacheagent]
    (let [email    (:email config)
          password (:password config)
          token    (get-token email password)]
      (try+
       (println "Refreshing Exzigo cache using token: " token)
       (refresh-jobsites token)
       (refresh-employees token)
       (Thread/sleep (:interval config))

       (catch [:type ::authentication] {:keys [message]}
         (println "Poller failed to authenticate and fetch updates."))))
    (recur config)))

