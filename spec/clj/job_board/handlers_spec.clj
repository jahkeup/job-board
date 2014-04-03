(ns job-board.handlers-spec
  (:use job-board.spec-helper speclj.core)
  (:require [job-board.handlers :as h]
            [job-board.authentication :as auth]
            [ring.mock.request :refer :all]))

(describe "Application handlers"
          (describe "auth handler"
            (it "should allow the whitelisting of pages"
                (let [valid-req (request :get "/login")
                      auth (h/require-auth-except-for ["/login"])
                      expected-body {:status 200 :body "Some body"}
                      handler (auth (constantly expected-body))]
                  (should= expected-body (handler valid-req))))

            (it "should not allow visits without authentication"
                (let [invalid-req (request :get "/assignments")
                      auth (h/require-auth-except-for [])
                      body (constantly {:status 200 :body "some body"})
                      handler (auth body)]
                  (should-not= (body) (handler invalid-req))
                  (should= (body) (let [valid-req
                                        (assoc invalid-req :cookies
                                               {:token {:value (auth/new-token)}})]
                                    (handler valid-req)))))))


(def handler ((h/require-auth-except-for ["/login"]) #(constantly {:status 200})))

(defn arg-count [f]
  (let [m (first (.getDeclaredMethods (class f)))
        p (.getParameterTypes m)]
    (alength p)))

(arg-count handler)
