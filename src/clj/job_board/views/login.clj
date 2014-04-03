(ns job-board.views.login
  (:require [job-board.views.layout :refer [page]]
            [hiccup.bootstrap3.forms :refer :all]
            [hiccup.bootstrap3.elements :refer :all]
            :reload))


(defn login-form []
  (col-row {:size 8}
           [:div {:style "height: 100px"}]
           [:h1 "Please login to continue"]
           (col-row {:size 6}
                    (form {:action "/login" :method "post"}
                          (input "pin" "Pin" "password" "****")
                          (button {:type "submit" :class "btn-success"} "Login")))))

(defn login-page []
  (page (login-form)))

