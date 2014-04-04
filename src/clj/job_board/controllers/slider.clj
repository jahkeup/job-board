(ns job-board.controllers.slider
  (:require [org.httpkit.server :refer :all]
            [job-board.views.slider :as view]
            [job-board.pages :refer [page-dump]]
            [job-board.slider :refer [join-send-slide drop-channel]]))

(defn current [request]
  (with-channel request chn
    (on-close chn #(do (drop-channel chn)
                       (println "Disconnect client: " %)))
    (join-send-slide chn)))

(defn slides []
  {:body (view/slides)
   :headers {"Content-Type" "text/html"}})


