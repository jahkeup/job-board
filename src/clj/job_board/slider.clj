(ns job-board.slider
  (:require [org.httpkit.server :refer :all]
            [korma.core :as db]
            [job-board.database :as m]
            [job-board.views.slider :as view]))

;; These are the active channels
(def broadcast-channels (atom []))

;; Eventually a logging func
(defn end-broadcast [message]
  (prn message))

;; Current slide map
(def current-slide-id (atom 0))

;; Return the current slide map
(defn current-slide []
  (if (= 0 @current-slide-id) {:name "No Assignments"}
      (let [jobsite (db/select m/assignments
                               (db/where {:jobsite_id @current-slide-id}))
            employees (db/select m/employees)]
        "hi")))

(defn send-channel-slide [channel slide]
  (send! channel slide))

(defn broadcast-slide-to [channels slide]
  (doall
   (for [channel channels]
     (send-channel-slide channel slide))))

(defn broadcast-new-slide
  "Broadcast a slide to the connected parties"
  [slide]
  (let [active-channels (swap! broadcast-channels
                               (filter open? @broadcast-channels))
        slide (view/slide slide)]
    (if (= 0 (count active-channels)) (end-broadcast "no clients")
        (broadcast-slide-to active-channels slide))))


(defn join-send-slide [channel]
  (swap! broadcast-channels (conj @broadcast-channels channel))
  (send! channel "hi"))
