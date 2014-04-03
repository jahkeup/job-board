(ns job-board.controllers.assignments
  (:require [schema.core :as s]
            [schema.coerce :as coerce]
            [job-board.database :refer :all]
            [korma.core :as db]
            [cheshire.core :only [generate-string]]
            [job-board.views.assignments :as view]))


;; Load all assignments by default and then narrow down in usage.
(def ^:private all-assignments (-> (db/select* assignments)
                                   (db/where {:employees.active true})
                                   (db/with employees)))

;; Select a jobsite
(def ^:private one-jobsite (-> (db/select* jobsites)
                               (fields :id)
                               (db/limit 1)))

;; (def EmployeeAssignmentParams
;;   {:employee_ids [s/Int]
;;    :jobsite_id s/Int})

;; (def parse-bulk-assignment
;;   (coerce/coercer EmployeeAssignmentParams coerce/json-coercion-matcher))

(defn indx
  "List all assignments"
  []
  (let [assignments (db/select all-assignments
                               (with jobsites))
        available-jobsites (db/select jobsites)]
    {:body (cheshire.core/generate-string assignments)
     :headers {"Content-Type" "application/json"}}))

(defn jobsite-info-for
  "Given a jobsite map with its id, return the assignees and jobsite in a map"
  [jobsite]
  {:jobsite jobsite
   :employees (db/select all-assignments
                         (where {:jobsite_id (:id jobsite)}))})

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
  [request]
  "")
