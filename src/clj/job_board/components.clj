(ns job-board.components
  (:require [com.stuartsierra.component :as component]
            [org.httpkit.server :refer [run-server]]
            [job-board.handlers :as handlers]
            [job-board.slider :as slider]
            [job-board.exzigo-cache :as cacher]))

(defrecord Webserver [port]
  component/Lifecycle
  (start [component]
    (println ";; Starting Webserver")
    (let [stop-server (run-server #'handlers/app {:port port})]
      (assoc component :stop-func stop-server)))
  (stop [component]
    (println ";; Stopping Webserver")
    ((:stop-func component))
    component))

(defn new-webserver [port]
  (map->Webserver {:port port}))

(defrecord Broadcaster [rotations refresh-time]
  component/Lifecycle
  (start [component]
    (println ";; Starting Broadcaster")
    (let [broadcaster (future (slider/run-broadcaster rotations refresh-time))]
      (assoc component :running-future broadcaster)))

  (stop [component]
    (let [f (:running-future component)]
      (if (not (future-done? f))
        (do
          (println ";; Stopping Broadcaster")
          (future-cancel f))
        (println ";; Broadcaster already stopped."))
      component)))

(defn new-broadcaster [rotations refresh-time]
  (map->Broadcaster {:rotations rotations :refresh-time refresh-time}))


(defrecord CacheAgent [email password]
  component/Lifecycle
  (start [component]
    (println ";; Starting Caching Agent"))
  (stop [component]
    (println ";; Stopping Caching Agent")))

(defn new-cache-agent [email password]
  (map->CacheAgent {:email email :password password}))
