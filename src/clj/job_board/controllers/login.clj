(ns job-board.controllers.login
  (:require [job-board.views.login :as view]
            [clojure.string :refer [blank?]]
            [ring.util.response :refer [set-cookie redirect]]
            [job-board.authentication :as auth]))

(defn login [] {:status 200 :body (view/login-page)
                :headers {"Content-Type" "text/html"}})

(defn authenticate [pin]
  (if (blank? pin) (redirect "/login")
      (if-let [token (auth/authenticate pin)]
        (->
         (redirect "/assignments")
         (set-cookie :token token))
        (redirect "/login"))))
