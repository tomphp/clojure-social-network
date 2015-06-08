(defproject social-network "0.1.0-SNAPSHOT"
  :description "Social Network Kata"
  :url "http://github.com/tomphp/clojure-social-network"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[speclj "2.5.0"]]}}
  :plugins [[lein-cucumber "1.0.2"]
            [speclj "2.5.0"]]
  :test-paths ["spec"])
