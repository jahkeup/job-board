(ns job-board.pages
  (:require [job-board.views.layout :as layout]
            [hiccup.core]))

(defn about-page []
  (layout/page [:div.col-sm-12
                [:h1 "About Page"]]))

(defn not-found []
  (layout/page [:div.col-sm-12
                [:h1.danger "Page not found"]]))

(defn page-dump [data]
  {:body (layout/page [:h1 "Page Dump Incoming!!!"] [:pre (with-out-str (clojure.pprint/pprint data))])
   :headers {"Content-Type" "text/html"}})
