(ns job-board.views.assignments
  (:require [hiccup.core :refer :all]
            [hiccup.bootstrap3.forms :as f]
            [hiccup.bootstrap3.elements :refer :all]
            [clojure.string :as string]
            [job-board.views.layout :refer :all]))

(defn assign-select
  "Create a select dropdown given the jobsites"
  [jobsites]
  (f/select-alone "assign_to" "Assign To Jobsite"
                  (map #(identity {:name (:name %) :value (:id %)})
                       (conj jobsites {:name "Unassign" :id 0}))))

(defn assignments-edit-list [avail-jobsites assignments]
  (page
   [:div {:class "top-matter"}
    [:h1 "Assign Employees"]]
   (f/form {:action "/assignments" :method "post"}
           (col-row
            {:size 12}
            [:div.row
             (col {:size 6} (assign-select avail-jobsites))
             (col {:size 6} [:div.form-group
                             (button {:type "submit"
                                      :class "btn-primary"
                                      :style "margin-top: 25px"} "Assign")])]
            [:table.table.table-hover.table-condensed
             [:thead
              [:tr
               [:td.col-md-1 "Reassign"]
               [:td "Employee"]
               [:td "Assigned Jobsite"]]]
             [:tbody
              (for [assignment assignments]
                [:tr
                 [:td.reassign
                  [:input {:type "checkbox"
                           :name "employee_ids[]"
                           :value (:employee_id assignment)}]]
                 [:td.employee
                  (string/join " " [(:first_name assignment)
                                    (:last_name assignment)])]
                 [:td.assigned
                  (:name assignment)]])]] ;; end table
            ))))
