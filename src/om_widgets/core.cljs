(ns om-widgets.core
    (:require [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]
              [cljsjs.react-widgets]))


;; ====================================================================== 
;; React Widgets Wrappers

(defn dropdown-list [data owner {:keys [val-key menu-key]}]
  (reify
    om/IRender
    (render [_]
      (js/React.createElement js/ReactWidgets.DropdownList 
        #js {:defaultValue (get data val-key) 
             :data (clj->js (get data menu-key))
             :onChange (fn [new-val]
                         (om/update! data val-key new-val))}))))

(defn date-time-picker [data owner {:keys [date-key]}]
  (reify
    om/IRender
    (render [_]
      (js/React.createElement js/ReactWidgets.DateTimePicker
        #js {:time false
             :value (get data date-key) 
             :onChange (fn [d s]
                         (om/update! data date-key d))}))))

(defn autocomplete [data owner {:keys [menu-key val-key]}]
  (reify
    om/IRender
    (render [_]
      (let [menu (get data menu-key)]
        (js/React.createElement js/ReactWidgets.Combobox
          #js {:data (clj->js menu)
               :suggest true
               :onSelect (fn [new-val]
                           (when (contains? (set menu) new-val)
                             (om/update! data val-key new-val)))})))))

