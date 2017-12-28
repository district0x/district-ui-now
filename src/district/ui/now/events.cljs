(ns district.ui.now.events
  (:require
    [cljs-time.core :as t]
    [district.ui.now.queries :as queries]
    [district0x.re-frame.interval-fx]
    [re-frame.core :refer [reg-event-fx trim-v]]))

(def interceptors [trim-v])

(reg-event-fx
  ::start
  interceptors
  (fn [{:keys [:db]} []]
    (merge
      {:db (queries/assoc-now db (t/now))
       :dispatch-interval {:dispatch [::update-now]
                           :id ::update-now
                           :ms 1000}})))


(reg-event-fx
  ::update-now
  interceptors
  (fn [{:keys [:db]}]
    {:db (queries/assoc-now db (t/now))}))


(reg-event-fx
  ::stop
  interceptors
  (fn [{:keys [:db]}]
    {:db (queries/dissoc-now db)
     :clear-interval {:id ::update-now}}))

