(ns job-board.database
  (:require [korma.db :refer :all]
            [korma.core :refer :all]
            [job-board.config.database :as config]))


;; Database connection specs
(def conn-spec (postgres (merge config/connection
                                {:delimiters ""})))

;; Load up the database per map specs
(defdb db conn-spec)

;; Let us predeclare our bullshit "models"
(declare employees jobsites assignments)

(defentity employees
  (entity-fields :first_name :last_name :active)
  (has-one assignments {:fk :employee_id}))

(defentity jobsites
  (entity-fields :id :name :address :city :state)
  (has-one assignments {:fk :jobsite_id}))

(defentity assignments
  (table :employees_jobsites)
  (entity-fields :employee_id :jobsite_id)
  (belongs-to employees {:fk :employee_id})
  (belongs-to jobsites {:fk :jobsite_id}))

(defentity assigned-employees
  (table (subselect assignments
                    (fields :employees.id :employees.first_name :employees.last_name)
                    (where {:employees.active true})
                    (join employees (= :employees.id :employees_jobsites.employee_id))) :assigned_employees))

(defentity unassigned-employees
  (table (subselect employees
                    (where* "employees.id NOT IN (SELECT employee_id FROM employees_jobsites)"))
         :unassigned_employees))

;; (sql-only (select assigned-employees))
;; (sql-only (select unassigned-employees))
;; (select assigned-employees)
;; (select employees
;;         (where {:active true})
;;         (with assignments (with jobsites)))

