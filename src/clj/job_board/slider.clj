(ns job-board.slider
  (:require [org.httpkit.server :refer [send! open?]]
            [korma.core :as db]
            [job-board.database :as m]
            [job-board.views.slider :as view]))

;; These are the active channels
(def broadcast-channels (atom #{}))

;; Eventually a logging func
(defn end-broadcast [message]
  (prn message))

;; Current slide map
(def current-slide-id (atom 0))

(defn get-jobsite
  "Get jobsite for jobsite-id"
  [jobsite-id]
  (first (db/select m/jobsites (db/where {:id jobsite-id}))))

(defn get-assignees
  "Get assignees assigned to the jobsite-id"
  [jobsite-id]
  (let [assignees-ids (map :employee_id (db/select m/assignments
                                                   (db/where {:jobsite_id jobsite-id})))
        assignees (db/select m/employees (db/where {:id [in assignees-ids]}))]
    assignees))

(defn get-assigned-jobsites []
  (set (map :jobsite_id (db/select m/assignments))))

;; Return the current slide map
(defn current-slide
  "Return the current slide's data (html)"
  []
  (if (= 0 @current-slide-id) (view/slide {:name "No Assignments"})
      (let [jobsite-id @current-slide-id
            jobsite (get-jobsite jobsite-id)
            employees (get-assignees (:id jobsite))
            slide (merge jobsite {:employees employees})]
        (view/slide slide))))

(defn send-channel-slide
  "Send slide to single channel"
  [channel slide]
  (send! channel slide))

(defn broadcast-slide-to
  "Send slide data off to channels"
  [channels slide]
  (doall
   (for [channel channels]
     (send-channel-slide channel slide))))

(defn cleanup-channels
  "Return channels that are actually open"
  [channels]
  (filter open? channels))

(defn broadcast-slide
  "Broadcast a slide to the connected parties"
  [slide]
  (let [active-channels (swap! broadcast-channels cleanup-channels)]
    (if (= 0 (count active-channels))
      (end-broadcast "no clients")
      (broadcast-slide-to active-channels slide))))

(defn drop-channel
  "Removes channel from the active list"
  [channel]
  (swap! broadcast-channels disj channel))

(defn join-send-slide [channel]
  (swap! broadcast-channels conj channel)
  (send! channel (current-slide)))

(defn display-slides-once
  "Run through slides one time with a delay of time between broadcasts."
  [slide-ids time]
  (doall (for [id slide-ids]
           (do (reset! current-slide-id id)
               (println (str "Sending slide for jobsite " @current-slide-id))
               (broadcast-slide (current-slide))
               (Thread/sleep time)))))

(defn run-broadcaster
  "Run slides with slide-time (in secs) between each should be used in
   a future (INFINITELY AWE$OME)"
  [slide-time loop-refresh]
  (let [t (* slide-time 1000)]
    (loop [slides (get-assigned-jobsites) reps 1]
      (display-slides-once slides t)
      (if (= loop-refresh reps)
        (recur (get-assigned-jobsites) 0)
        (recur slides (inc reps))))))
