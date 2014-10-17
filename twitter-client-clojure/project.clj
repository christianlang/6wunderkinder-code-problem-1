(defproject twitter-client "0.1.0-SNAPSHOT"
  :url "http://github.com/christianlang/6wunderkinder-code-problem-1"
  :dependencies [[org.clojure/clojure "1.6.0"]
  				 [clj-http "1.0.0"]
  				 [org.clojure/data.codec "0.1.0"]
  				 [org.clojure/data.json "0.2.5"]]
  :main ^:skip-aot twitter-client.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
