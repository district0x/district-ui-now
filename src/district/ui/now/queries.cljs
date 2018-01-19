(ns district.ui.now.queries
  (:require
    [cljs-time.coerce :refer [to-long]]
    [district.time :as time]))

(defn now [db]
  (-> db :district.ui.now :now))

(defn assoc-now [db now]
  (assoc-in db [:district.ui.now :now] now))

(defn dissoc-now [db]
  (dissoc db :district.ui.now))

(defn time-remaining [db to-time]
  (time/time-remaining (now db) to-time))