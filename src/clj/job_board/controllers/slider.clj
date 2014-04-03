(ns job-board.controllers.slider
  (:require [org.httpkit.server :refer :all]
            [job-board.views.slider :as view]
            [job-board.pages :refer [page-dump]]
            [job-board.slider :refer [join-send-slide]]))



(defn current [request]
  (with-channel request chn
    (send! chn "hi")))


(defn slides []
  {:body (view/slides)
   :headers {"Content-Type" "text/html"}})


