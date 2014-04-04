(ns job-board.controllers.assignments-spec
  (:require [speclj.core :refer :all]
            [job-board.spec-helper :refer :all]
            [job-board.controllers.assignments :as assignments]))


(describe "Assignments controller"
          (it "should validate assignment params"
              (let [req-data {:employee_ids [1 2 3] :jobsite_id 4}]
                (should-not-be-nil (assignments/parse-bulk-assignment req-data))
                (should-be-nil (assignments/parse-bulk-assignment (dissoc req-data :jobsite_id)))))
          (it "should list the assignments for all the employees")
          (it "should list the assignments for a given jobsite")
          (it "should return an error on assignment post with invalid data"))
