(defproject om-widgets "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "0.0-3308"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [bensu/react-widgets "2.6.1-0"]
                 [cljsjs/moment "2.9.0-0"]
                 [org.omcljs/om "0.8.8"]]

  :plugins [[lein-cljsbuild "1.0.5"]
            [lein-figwheel "0.3.5"]]

  :source-paths ["src"]

  :clean-targets ^{:protect false} ["example/resources/public/js/compiled" "target"]

  :cljsbuild {
    :builds [{:id "dev"
              :source-paths ["examples/showcase/src"]

              :figwheel {:on-jsload "showcase.core/on-js-reload" }

              :compiler {:main showcase.core 
                         :asset-path "js/compiled/out"
                         :output-to "resources/public/js/compiled/showcase.js"
                         :output-dir "resources/public/js/compiled/out"
                         :source-map-timestamp true }}
             {:id "min"
              :source-paths ["examples/showcase/src"]
              :compiler {:output-to "resources/public/js/compiled/showcase.js"
                         :main showcase.core
                         :optimizations :advanced
                         :pretty-print false}}]}

  :figwheel {:css-dirs ["resources/public/css"]})
