(ns job-board.views.slider
  (:require [hiccup.core :refer :all]
            [hiccup.page :refer [include-js]]
            [hiccup.bootstrap3.elements :refer :all]
            [job-board.views.layout :refer [page]]))

(defn slides []
  (page
   [:div#slide-canvas]
   (include-js "/static/js/slider.js")
   [:script "connect();"]))


(defn slide
  "Prepare slide to be sent over websocket to client, where slide is a
  map of the jobsite and its employees"
  [slide]
  (html [:div.slide.center-block
         [:h1.name (:name slide)]
         [:div.employees
          [:h5.employees-title "Working this jobsite:"]
          [:ul
           (doall (for [employee (:employees slide)]
                    [:li (str (:first_name employee) " " (:last_name employee))]))]]]))
