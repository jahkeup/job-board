(ns job-board.views.slider
  (:require [hiccup.core :refer :all]
            [hiccup.bootstrap3.elements :refer :all]
            [job-board.views.layout :refer [page]]))

(defn slides []
  (page
   [:div#slide-canvas]
   [:script {:src "js/slider.js"}]))


(defn slide [slide]
  ([:div.slide]))
