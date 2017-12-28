(ns district.ui.now.queries
  (:require [cljs-time.coerce :refer [to-long]]))

(defn now [db]
  (-> db :district.ui.now :now))

(defn assoc-now [db now]
  (assoc-in db [:district.ui.now :now] now))

(defn dissoc-now [db]
  (dissoc db :district.ui.now))

(defn time-duration-units [milis]
  {:seconds (js/Math.floor (mod (/ milis 1000) 60))
   :minutes (js/Math.floor (mod (/ (/ milis 1000) 60) 60))
   :hours (js/Math.floor (mod (/ milis 3600000) 24))
   :days (js/Math.floor (/ milis 86400000))})

(defn time-remaining [db to-time]
  (let [milis-difference (- (to-long to-time) (to-long (now db)))]
    (if (pos? milis-difference)
      (time-duration-units milis-difference)
      {:seconds 0 :minutes 0 :hours 0 :days 0})))