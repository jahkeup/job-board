(ns job-board.config.system
  (:require [job-board.components :refer :all]
            [job-board.config.utils :refer [parse-int]]
            [com.stuartsierra.component :as component]
            [environ.core :refer [env]]))

(defn job-board-system [config]
  (let [{:keys [port slide-refresh slide-time]} config]
    (component/system-map
     :config config
     :webserver (new-webserver (parse-int (env :webserver-port)))
     :broadcaster (new-broadcaster slide-refresh slide-time)
     :cacheagent (new-cache-agent (env :exzigo-username)
                                  (env :exzigo-password)
                                  (parse-int (env :exzigo-poll-interval))))))

(def system (job-board-system {:port 3000 :slide-refresh 1
                               :slide-time 10}))

(defn start-system []
  (alter-var-root #'system component/start))

(defn stop-system []
  (alter-var-root #'system component/stop))

