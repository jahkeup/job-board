(ns job-board.database
  (:use korma.db korma.core environ.core))


;; Database connection specs
(def pg (postgres {:db "jobboard"
                   :user "Jacob"
                   :password ""
                   :delimiters ""}))

;; Load up the database per map specs
(defdb db pg)

;; Let us predeclare our bullshit "models"
(declare employees jobsites assignments)

(defentity employees
  (entity-fields :first_name :last_name :active)
  (has-one assignments {:fk :employee_id}))

(defentity jobsites
  (entity-fields :name :address :city :state)
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


(defn )

;; (sql-only (select assigned-employees))
;; (sql-only (select unassigned-employees))
;; (select assigned-employees)
;; (select employees
;;         (where {:active true})
;;         (with assignments (with jobsites)))

