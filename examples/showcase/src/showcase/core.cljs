(ns ^:figwheel-always showcase.core
    (:require [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]
              [om-widgets.core :as widgets]
              [om-widgets.date-adapters :as date-adapters]))

(enable-console-print!)

;; ====================================================================== 
;; Wrapper Config

(date-adapters/set! (date-adapters/moment-adapter))

;; ====================================================================== 
;; App

(def colors ["orange" "red" "blue" "purple"])

(def fruits ["orange" "apple" "strawberry" "banana"])

(defonce app-state (atom {:text "Hello World"
                          :date nil
                          :colors colors
                          :color "red"
                          :fruit "banana"
                          :fruits fruits}))

(defn main [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
        (dom/h1 nil (:text data))
        (om/build widgets/date-time-picker data {:opts {:date-key :date}})
        (om/build widgets/dropdown-list data {:opts {:val-key :color
                                                     :menu-key :colors}})
        (om/build widgets/autocomplete data {:opts {:val-key :fruit
                                                    :menu-key :fruits}})))))

(om/root main app-state {:target (. js/document (getElementById "app"))})

(defn on-js-reload []) 
