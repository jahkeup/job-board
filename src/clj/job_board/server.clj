(ns job-board.server
  (:require [job-board.config.system :refer [start-system]])
  (:gen-class))

(defn -main [& args]
  (start-system))
