(ns job-board.views.layout
  (:use hiccup.core hiccup.page)
  (:require [hiccup.bootstrap3.elements :refer :all]))


(defn- head-content []
  [:head
   [:meta {:charset "utf-8"}]
   (include-css "/static/bower_components/bootstrap/dist/css/bootstrap.min.css")
   (include-js  "/static/bower_components/jquery/dist/jquery.min.js")
   (include-js  "/static/bower_components/bootstrap/dist/js/bootstrap.min.js")
   (include-css "/static/css/page.css")])

(defn page
  "Main layout"
  [& body]
  (html5
   (head-content)
   [:body
    (container
     (col-row {:size 12}
              body))]))

