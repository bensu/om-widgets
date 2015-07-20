(ns om-widgets.date-adapters
  (:require [cljsjs.react-widgets]
            [cljsjs.moment]))

;; ====================================================================== 
;; Global Config

(defn set! [adapter]
  (.setDateLocalizer js/ReactWidgets.configure adapter));

;; ====================================================================== 
;; Adapters

(defn moment-adapter []
  (letfn [(end-of-decade [d]
            (.toDate (.add (.add (js/moment d) 10 "year") -1 "millisecond")))
          (end-of-century [d]
            (.toDate (.add (.add (js/moment d) 100 "year") -1 "millisecond")))]
    #js {:formats #js {:date "L"
                       :time "LT"
                       :default "lll"
                       :header "MMMM YYYY"
                       :footer "LL"
                       :weekday (fn [d c]
                                  (-> (js/moment)  
                                    (.weekday d)
                                    (.format "dd")))
                       :dayOfMonth "DD"
                       :month "MMM"
                       :year "YYYY"
                       :decade (fn [d c localizer]
                                 (str (.format localizer d "YYYY" c) " - "
                                   (.format localizer (end-of-decade d) "YYYY" c)))}
         :firstOfWeek (fn [c]
                        (.firstDayOfWeek (.localeData js/moment c)))
         :parse (fn [v f c]
                  (.toDate (js/moment v f)))
         :format (fn [v f c]
                   (.format (js/moment v) f))}))
