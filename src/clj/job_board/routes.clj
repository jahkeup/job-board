(ns job-board.routes
  (:use compojure.core)
  (:require [compojure.route :as route]
            [clojure.pprint :refer [pprint]]
            [ring.util.response :refer [redirect]]
            [job-board.pages :as pages]
            [job-board.controllers.login :as login]
            [job-board.controllers.employees :as employees]
            [job-board.controllers.assignments :as assignments]))

(defn redirect-if [fi thn-path els-path]
  (redirect (if fi thn-path els-path)))

(defroutes approutes
  (GET  "/" [:as req]
        (redirect-if (:authenticated? req) "/assignments" "/login"))
  (GET "/dump" [:as req] {:body (with-out-str (pprint req))
                             :headers {"Content-Type" "text/plain"}})
  (GET  "/login" {authed :authenticated?} [] (if authed (redirect "/")
                                                 (login/login)))
  (POST "/login" [:as request] (login/authenticate (:pin (:params request))))
  (GET  "/assignments" [] (assignments/indx))
  (GET  "/assignments/:id" [id] (assignments/for-jobsite id))
  (POST "/assignments" [:as request] (assignments/assign request))
  (GET  "/employees/:id" [id] (employees/show id))
  (POST "/employees/:id/unassign" [id] (employees/unassign id))
  (route/not-found (pages/not-found)))

