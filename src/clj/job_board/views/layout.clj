(ns job-board.views.layout
  (:use hiccup.core hiccup.page)
  (:require [hiccup.bootstrap3.elements :refer :all]))


(defn- head-content []
  [:head
   (include-css "bower_components/bootstrap/dist/css/bootstrap.min.css")
   (include-js "bower_components/jquery/dist/jquery.min.js")
   (include-js "bower_components/boostrap/dist/js/bootstrap.min.js")])

(defn page
  "Main layout"
  [body]
  (html5
   (head-content)
   [:body
    (container
     (col-row {:size 12}
              body))]))

