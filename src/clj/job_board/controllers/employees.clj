(ns job-board.controllers.employees
  (:require [korma.core :refer [select where]]
            [job-board.database :refer [employees]]))

(defn unassign [] "")

(defn show
  "show employee with id"
  [id]
  {:body {:employee (first (select employees (where {:id id})))}})

