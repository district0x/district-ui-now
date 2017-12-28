(ns district.ui.now.subs
  (:require
    [district.ui.now.queries :as queries]
    [re-frame.core :refer [reg-sub]]))

(defn- sub-fn [query-fn]
  (fn [db [_ & args]]
    (apply query-fn db args)))

(reg-sub
  ::now
  queries/now)

(reg-sub
  ::time-remaining
  (sub-fn queries/time-remaining))
