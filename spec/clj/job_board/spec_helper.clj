(ns job-board.spec-helper
  (:require [clojure.string :as string]
            [speclj.core :refer :all]))

(defn should-be-blank [body]
  (should (string/blank? body)))

(defn should-not-be-blank [body]
  (should-not (string/blank? body)))
