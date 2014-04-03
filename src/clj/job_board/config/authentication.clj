(ns job-board.config.authentication
  (:require [environ.core :refer [env]]))

(def pin (or (env :auth-pin) "4312"))

