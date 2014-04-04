(ns job-board.server-spec
  (:require [ring.mock.request :refer [request body] :rename {body set-body}]))

(set-body (request :get "/") "")
