(ns ^:figwheel-always om-widgets.core
    (:require [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]
              [cljsjs.react-widgets]))

;; ====================================================================== 
;; Helpers

(defn ->seq [korks]
  (if (coll? korks)
    korks
    [korks]))

(defn -get [m korks]
  (get-in m (->seq korks)))

(defn find-by-key [menu k v]
  (->> menu 
    (filter #(= v (-get % k)))
    (first)))

;; ====================================================================== 
;; React Widgets Wrappers

(defn dropdown-list
  "Creates a dropdown component from data. Takes an opts maps with:
   :menu-key - The korks path to the menu in data 
   :val-key - The korks path to the value in data
   :id-key - The korks path to the id of each item in the menu
   :label-key - The korks path to the label of each item in the menu
   :props - A map with Js Objects to be directly fed into the React Component.
  
   Ex:

   (let [data {:colors [{:id 1 :name \"red\"} {:id 2 :name \"blue\"}]
               :color-id 1}]
     (om/build dropdown-list 
               data
               {:opts {:val-key :color-id
                       :id-key :id
                       :label-key :name
                       :menu-key :colors
                       :props {:open true}}}))"
  [data owner {:keys [val-key menu-key id-key label-key props]}]
  (reify
    om/IRender
    (render [_]
      (let [menu (-get data menu-key)]
        (js/React.createElement js/ReactWidgets.DropdownList 
          (-> {:defaultValue (-> (find-by-key menu id-key (-get data val-key))
                               (-get label-key)) 
               :data (mapv #(-get % label-key) menu)
               :onChange (fn [new-val]
                           (let [new-id (-> (find-by-key menu label-key new-val)
                                          (-get id-key))]
                             (om/update! data val-key new-id)))}
            (merge props)
            clj->js))))))

(defn date-time-picker
 "Creates a date picker. Takes an opts maps with:
   :date-key - The korks path to where the selected date (a native inst)
               should be stored.
   :props - A map with Js Objects to be directly fed into the React Component.

   Ex:

   (let [data {:date {:inst (js/Date.)}}]
     (om/build date-time-picker data {:opts {:date-key [:date :inst]}
                                             :props {:time true}}))"
  [data owner {:keys [date-key props]}]
  (reify
    om/IRender
    (render [_]
      (js/React.createElement js/ReactWidgets.DateTimePicker
        (-> {:time false
             :value (-get data date-key) 
             :onChange (fn [d s]
                         (om/update! data date-key d))}
          (merge props)
          clj->js)))))

(defn autocomplete
  "Creates an autocomplete component from data. Takes an opts maps with:
   :menu-key - The korks path to the menu in data 
   :val-key - The korks path to the value in data
   :id-key - The korks path to the id of each item in the menu
   :label-key - The korks path to the label of each item in the menu
   :props - A map with Js Objects to be directly fed into the React Component.
  
   Ex:

   (let [data {:colors [{:id 1 :name \"red\"} {:id 2 :name \"blue\"}]
               :color-id 1}]
     (om/build autocomplete-list 
               data
               {:opts {:val-key :color-id
                       :id-key :id
                       :label-key :name
                       :menu-key :colors
                       :props {:open true}}}))"
  [data owner {:keys [menu-key val-key label-key id-key props]}]
  (reify
    om/IInitState
    (init-state [_]
      {:phrase (-get (or (find-by-key (-get data menu-key) 
                           id-key (-get data val-key))
                       {label-key ""})
                 label-key)})
    om/IRenderState
    (render-state [_ {:keys [phrase]}]
      (let [menu (-get data menu-key)
            labels (mapv #(-get % label-key) menu)]
        (js/React.createElement js/ReactWidgets.Combobox
          (-> {:data labels 
               :value phrase
               :suggest true
               :onChange (fn [p]
                           (om/set-state! owner :phrase p))
               :onSelect (fn [new-val]
                           (when (contains? (set labels) new-val)
                             (let [id (-> (find-by-key menu label-key new-val)
                                        (-get id-key))]
                               (om/update! data val-key id))))}
            (merge props)
            clj->js))))))

