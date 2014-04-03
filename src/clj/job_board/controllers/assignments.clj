(ns job-board.controllers.assignments
  (:require [schema.core :as s]
            [schema.coerce :as coerce]
            [job-board.database :refer :all]
            [korma.core :as db]
            [cheshire.core :only [encode]]))

(def ^:private all-assignments (-> (db/select* assignments)
                                   (db/where {:employees.active true})
                                   (db/with employees)
                                   (db/with jobsites)))

(def EmployeeAssignmentParams
  {:employee_ids [s/Int]
   :jobsite_id s/Int})

(def parse-bulk-assignment
  (coerce/coercer EmployeeAssignmentParams coerce/json-coercion-matcher))

(defn indx
  "List all assignments"
  []
  {:body (chesire.core/encode (db/select all-assignments))
   :headers {"Content-Type" "application/json"}})

(defn for-jobsite
  "List assignments for the specified jobsite"
  [jobsite-id]
  ())

(defn assign
  "Assign employees to a jobsite.

   This should be a POST request with employee_ids[] and jobsite_id"
  [request]
  "")
