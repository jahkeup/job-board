(ns job-board.config.system
  (:require [job-board.components :refer :all]
            [job-board.config.utils :refer [parse-int]]
            [com.stuartsierra.component :as component]
            [environ.core :refer [env]]))

(defn job-board-system [config]
  (let [{:keys [webserver-port slide-refresh slide-time
                exzigo-username exzigo-password
                exzigo-poll-interval]} config]
    (component/system-map
     :config config

     :webserver (new-webserver webserver-port)
     :broadcaster (new-broadcaster slide-time slide-refresh)
     :cacheagent (new-cache-agent exzigo-username
                                  exzigo-password
                                  exzigo-poll-interval))))

(def system (job-board-system {:webserver-port (or (parse-int (env :webserver-port)) 3000)

                               :slide-refresh (or (parse-int (env :slide-refresh)) 1)
                               :slide-time (or (parse-int (env :slide-time)) 10)

                               :exzigo-username (env :exzigo-username)
                               :exzigo-password (env :exzigo-password)
                               :exzigo-poll-interval (parse-int (env :exzigo-poll-interval))}))
(defn start-system []
  (alter-var-root #'system component/start))

(defn stop-system []
  (alter-var-root #'system component/stop))
