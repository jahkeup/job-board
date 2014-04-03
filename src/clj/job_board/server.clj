(ns job-board.server
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.resource :as resources]
            [ring.util.response :as response]
            [cheshire.core :as cheshire]
            [compojure.handler :as handler]
            [korma.core :refer :all]
            [job-board.routes :as routes]
            [job-board.authentication :as auth]
            [job-board.database :refer [employees]]
            [job-board.handlers :refer [app]])
  (:gen-class))

(defn start-server [join]
  (jetty/run-jetty #'app {:port 3000 :join? join}))

(defn -main [& args]
  (prn "Starting application"))
