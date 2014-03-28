(defproject job-board "0.1.0-SNAPSHOT"
  :description "Meade Job Board"
  :url "http://board.meade-internal.com"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2156"]
                 [ring "1.2.1"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]
                 [korma "0.3.0-RC5"]
                 [org.postgresql/postgresql "9.2-1002-jdbc4"]]
  :plugins [[lein-cljsbuild "1.0.2"]
            [lein-ring "0.8.10"]
            [speclj "2.5.0"]]
  :profiles {:dev {:dependencies [[speclj "2.5.0"]]}}
  :test-paths ["spec"] ;; speclj testing path
  :hooks [leiningen.cljsbuild]
  :source-paths ["src/clj"]
  :cljsbuild {
    :builds {
      :main {
        :source-paths ["src/cljs"]
        :compiler {:output-to "resources/public/js/cljs.js"
                   :optimizations :simple
                   :pretty-print true}
        :jar true}}}
  :main job-board.server
  :ring {:handler job-board.server/app})

