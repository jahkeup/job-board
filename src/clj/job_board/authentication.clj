(ns job-board.authentication
  (:require [crypto.password.bcrypt :as password]
            [crypto.random :as random]
            [job-board.config.authentication :as config]))

(def valid-period 3600000) ; TTL in ms.
(def valid-tokens (ref {}))
(def auth-pin (atom config/pin))

(defn- remove-token [token sec]
  (Thread/sleep sec)
  (dosync
   (ref-set valid-tokens (dissoc @valid-tokens (keyword token)))))

(defn new-token []
  (let [token (random/url-part 16)]
    (dosync
     (ref-set valid-tokens (assoc @valid-tokens (keyword token)
                                  (future (remove-token token valid-period)))))
    token))

(defn touch-token [token]
  (let [token (keyword token)]
    (if-let [expiry (token @valid-tokens)]
      (dosync (future-cancel expiry)
              (ref-set valid-tokens
                       (assoc @valid-tokens token
                              (future (remove-token token valid-period)))))))
  token)

(defn authenticate
  "Authenticate users with username + password. For now, this is the PIN check."
  [pin]
  (if (= (str pin) @auth-pin)
    (new-token)))

(defn authenticate-token [token]
  (if (contains? @valid-tokens (keyword token))
    (touch-token token)))

