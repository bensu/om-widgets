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

(def colors [{:id 1 :name "orange"}
             {:id 2 :name "red"}
             {:id 3 :name "blue"}
             {:id 4 :name "purple"}])

(def fruits [{:id 1 :name "orange"}
             {:id 2 :name "apple"}
             {:id 3 :name "strawberry"}
             {:id 4 :name "banana"}])

(defonce app-state (atom {:text "Hello World"
                          :date {:inst nil}
                          :colors colors
                          :color {:id 2} 
                          :fruit {:id 4} 
                          :fruits fruits}))

(defn main [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
        (dom/h1 nil (:text data))
        (om/build widgets/date-time-picker data {:opts {:date-key [:date :inst]}})
        (om/build widgets/dropdown-list data 
          {:opts {:val-key [:color :id]
                  :id-key :id
                  :label-key :name
                  :menu-key :colors}})
        (om/build widgets/autocomplete data
          {:opts {:val-key [:fruit :id]
                  :id-key :id
                  :label-key :name
                  :menu-key :fruits
                  :props {:open true}}})))))

(om/root main app-state {:target (. js/document (getElementById "app"))})

(defn on-js-reload []) 
