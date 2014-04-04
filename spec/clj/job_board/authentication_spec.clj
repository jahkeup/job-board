(ns job-board.authentication-spec
  (:require [speclj.core :refer :all]
            [job-board.spec-helper :refer :all]
            [job-board.authentication :as auth]
            [clojure.string :as string]))

(describe "Authentication"
          (it "should generate new tokens"
              (should-not-be-blank (auth/new-token)))

          (it "should have valid tokens for a set period"
              (with-redefs [auth/valid-period 1000]
                (let [token (auth/new-token)]
                  (should (auth/authenticate-token token))
                  ;; Authentication will reset timer, need to wait for
                  ;; it to expire.
                  (Thread/sleep (+ auth/valid-period 100))
                  (should-not (auth/authenticate-token token)))))

          (it "should be able to reset an expiry future for a token"
              (with-redefs [auth/valid-period 2000]
                (let [token (auth/new-token)
                      expire-f ((keyword token) @auth/valid-tokens)]
                  (auth/touch-token token)
                  (assert (true? (future-cancelled? expire-f)))
                  (assert (not
                           (true? (future-done?
                                       ((keyword token) @auth/valid-tokens))))))))

          (it "should return nil if unable to authenticate"
              (with-redefs [auth/auth-pin (atom "1234")]
               (should-be-nil (auth/authenticate 9992))))

          (it "should return the token if able to authenticate"
              (with-redefs [auth/new-token #(str "abcdefg")
                            auth/auth-pin (atom "1234")]
                (should= "abcdefg" (auth/authenticate 1234))))
          (it "should just return nil for a nil token"
              (should-be-nil (auth/authenticate-token nil))))

