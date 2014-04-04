(ns job-board.config.utils
  (:require [yaclot.core :as conv]
            [clojure.string :as s]))


(defn if-blank [string blank-fill]
  (if (s/blank? string)
    blank-fill
    string))

(defn parse-int [string]
  (if string
    (let [string (if-blank string "0")]
      (conv/convert string (conv/to-type Integer)))))

