 (defproject job-board "0.1.0-SNAPSHOT"
  :description "Meade Job Board"
  :url "http://board.meade-internal.com"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2156"]
                 [ring "1.2.1"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]
                 [korma "0.3.0"]
                 [crypto-random "1.2.0"]
                 [crypto-password "0.1.3"]
                 [org.postgresql/postgresql "9.3-1101-jdbc4"]
                 [cheshire "5.3.1"]
                 [prismatic/schema "0.2.1"]
                 [hiccup-bootstrap-elements "3.1.1-SNAPSHOT"]
                 [environ "0.4.0"]]
  :plugins [[lein-cljsbuild "1.0.2"]
            [lein-ring "0.8.10"]
            [speclj "2.5.0"]
            [drift "1.5.2"]
            [org.clojars.wokier/lein-bower "0.3.0"]]
  :profiles {:dev {:dependencies [[speclj "2.5.0"]
                                  [ring-mock "0.1.5"]
                                  [ring-server "0.3.1"]]}}
  :test-paths ["spec/clj/"]
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

