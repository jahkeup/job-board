(ns job-board.config.database
  (:require [environ.core :refer [env]]))

(def connection {:db       (or (env :db-database) "jobboard")
                 :user     (or (env :db-username) "jobboard")
                 :password (or (env :db-password) "jobboard")})

