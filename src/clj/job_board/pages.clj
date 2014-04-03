(ns job-board.pages
  (:require [job-board.views.layout :as layout]))

(defn about-page []
  (layout/page [:div.col-sm-12
                [:h1 "About Page"]]))

(defn not-found []
  (layout/page [:div.col-sm-12
                [:h1.danger "Page not found"]]))

