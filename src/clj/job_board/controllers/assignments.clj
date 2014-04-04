(ns job-board.controllers.assignments
  (:require [schema.core :as s]
            [schema.coerce :as coerce]
            [ring.util.response :refer [redirect]]
            [job-board.database :refer :all]
            [job-board.pages :refer [page-dump]]
            [korma.core :as db]
            [korma.db :refer [transaction]]
            [cheshire.core :only [generate-string]]
            [job-board.views.assignments :as view]))


;; Load all assignments by default and then narrow down in usage.
(def ^:private all-assignments (-> (db/select* assignments)
                                   (db/where {:employees.active true})
                                   (db/with employees)))

(def ^:private all-employees-w-assignments (-> (db/select* employees)
                                               (db/fields [:id :employee_id])
                                               (db/where {:active true})
                                               (db/with assignments
                                                        (db/with jobsites))))
;; Select a jobsite
(def ^:private one-jobsite (-> (db/select* jobsites)
                               (db/limit 1)))

(defn employee-assigned?
  "Is the employee assigned to anything?"
  [employee-id]
  (not (= 0 (count
         (db/select all-assignments
                    (db/where {:employee_id employee-id}))))))

(defn remove-assignment
  "Delete assignment for employee at jobsite"
  [employee-id]
  (db/delete assignments
             (db/where {:employee_id employee-id})))

(defn create-assignment
  "Create assignment for employee at jobsite (only used when unassigned)"
  [employee-id jobsite-id]
  (db/insert assignments
             (db/values [{:jobsite_id jobsite-id
                       :employee_id employee-id}])))

(defn update-assignment
  "Update the employee's assignment to jobsite"
  [employee-id jobsite-id]
  (db/update assignments
             (db/set-fields {:jobsite_id jobsite-id})
             (db/where {:employee_id employee-id})))

(defn assign-employee-jobsite
  "Update the employees assignment to the new jobsite,
   if the jobsite-id is 0, the user will be unassigned."
  [employee-id jobsite-id]
  (assert (and (number? jobsite-id) (number? employee-id)))
  (cond (= jobsite-id 0) (remove-assignment employee-id)
        (employee-assigned? employee-id) (update-assignment employee-id jobsite-id)
        :else (create-assignment employee-id jobsite-id)))

;; (def EmployeeAssignmentParams
;;   {:employee_ids [s/Int]
;;    :jobsite_id s/Int})

;; (def parse-bulk-assignment
;;   (coerce/coercer EmployeeAssignmentParams coerce/json-coercion-matcher))

(defn indx
  "List all assignments"
  []
  (let [assignments (db/select all-employees-w-assignments)
        available-jobsites (db/select jobsites)]
    {:body (view/assignments-edit-list available-jobsites assignments)
     :headers {"Content-Type" "text/html"}}
    ))

(defn jobsite-info-for
  "Given a jobsite map with its id, return the assignees and jobsite in a map"
  [jobsite]
  {:jobsite jobsite
   :employees (db/select all-assignments
                         (db/where {:jobsite_id (:id jobsite)}))})

(defn for-jobsite
  "List assignments for the specified jobsite"
  [jobsite-id]
  (let [id (read-string jobsite-id)]
    (if-let [jobsite (first (db/select
                             one-jobsite
                             (db/where {:id id})))]
      (let [jobsite-data (jobsite-info-for jobsite)]
        {:body (cheshire.core/generate-string jobsite-data)
         :headers {"Content-Type" "application/json"}}))))

(defn assign
  "Assign employees to a jobsite.

   This should be a POST request with employee_ids[] and jobsite_id"
  [update-ids to-id]
  (let [ids (map #(read-string %) update-ids)
        jobsite-id (read-string to-id)]
    (transaction
     (doall (for [employee-id ids]
              (assign-employee-jobsite employee-id jobsite-id)))
     (redirect "/assignments"))))


(for [employee-id '(90 48 65)]
  (assign-employee-jobsite employee-id 44))


