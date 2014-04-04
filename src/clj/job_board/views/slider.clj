(ns job-board.views.slider
  (:require [hiccup.core :refer :all]
            [hiccup.bootstrap3.elements :refer :all]
            [job-board.views.layout :refer [page]]))

(defn slides []
  (page
   [:div#slide-canvas]
   [:script {:src "js/slider.js"}]))


(defn slide
  "Prepare slide to be sent over websocket to client, where slide is a
  map of the jobsite and its employees"
  [slide]
  (html [:div.slide
         [:h1 (:name slide)]
         [:div.employees
          [:h5 "Working this jobsite:"]
          [:ul
           (doall (for [employee (:employees slide)]
                    [:li (str (:first_name employee) " " (:last_name employee))]))]]]))
