(ns job-board.views.assignments
  (:require [hiccup.core :refer :all]
            [hiccup.bootstrap3.forms :as f]
            [hiccup.bootstrap3.elements :refer :all]
            [job-board.views.layout :refer :all]))

(defn assign-select
  "Create a select dropdown given the jobsites"
  [jobsites]
  (f/select-alone "assign_to" "Assign To Jobsite"
                  (map #(identity {:name (:name %) :value (:id %)})
                       (conj jobsites {:name "Unassign" :id 0}))))

(defn assignments-edit-list [avail-jobsites assignments]
  (page (f/form {:action "/assign" :method "post"}
                (col-row {:size 12} (assign-select)))))
