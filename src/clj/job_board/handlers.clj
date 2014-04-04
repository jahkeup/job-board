(ns job-board.handlers
  (:require [compojure.handler :refer [site]]
            [ring.util.response :as resp]
            [ring.middleware.resource :as resources]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [ring.server.standalone :refer [serve]]
            [job-board.routes :as routes]
            [job-board.authentication :as auth]
            :reload))

(defn ignore-trailing-slash
  "Modifies the request uri before calling the handler.
  Removes a single trailing slash from the end of the uri if present.

  Useful for handling optional trailing slashes until Compojure's route matching syntax supports regex.
  Adapted from http://stackoverflow.com/questions/8380468/compojure-regex-for-matching-a-trailing-slash

  https://gist.github.com/dannypurcell/8215411"
  [handler]
  (fn [request]
    (let [uri (:uri request)]
      (handler (assoc request :uri (if (and (not (= "/" uri))
                                            (.endsWith uri "/"))
                                     (subs uri 0 (dec (count uri)))
                                     uri))))))

(defn check-auth-and-pass [next-handler request]
  (let [token (get-in request [:cookies "token" :value])]
    (if (auth/authenticate-token token)
      (let [authed-req (assoc request :authenticated? true)]
        (next-handler authed-req))
      (assoc (resp/redirect "/login") :authenticated? false))))

(defn require-auth-except-for
  "Require authentication by cookie except for [paths]"
  [paths]
  (fn [handler]
    (fn [request]
      (if (some #(= (:uri request) %) paths)
        (handler (assoc request :authenticated?
                        ((complement nil?)
                         (auth/authenticate-token
                          (get-in request [:cookies "token" :value])))))
        (check-auth-and-pass handler request)))))

(def app
  (let [auth-check
        (require-auth-except-for ["/" "/login"
                                  "/slides" "/slides/current"])]
    (-> routes/approutes
        auth-check
        ignore-trailing-slash
        (resources/wrap-resource "public")
        (wrap-content-type)
        (wrap-not-modified)
        site)))

;; (defonce running-server (serve #'app {:auto-reload? true}))

