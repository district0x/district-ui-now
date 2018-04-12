(ns tests.all
  (:require
    [cljs-time.core :as t]
    [cljs.test :refer [deftest is testing run-tests async use-fixtures]]
    [day8.re-frame.test :refer [run-test-async wait-for run-test-sync]]
    [district.ui.now.events :as events]
    [district.ui.now.subs :as subs]
    [district.ui.now]
    [mount.core :as mount]
    [re-frame.core :as re-frame :refer [reg-event-fx dispatch-sync subscribe reg-cofx reg-fx dispatch]]))

(use-fixtures
  :each
  {:after
   (fn []
     (mount/stop))})

(deftest tests
  (run-test-async
    (let [now (subscribe [::subs/now])
          time-remaining (subscribe [::subs/time-remaining (t/plus (t/now) (t/seconds 10))])
          *prev-now* (atom nil)
          *prev-time-remaining* (atom nil)]

      (-> (mount/with-args
            {})
        (mount/start))

      (reset! *prev-now* @now)
      (reset! *prev-time-remaining* @time-remaining)

      (wait-for [::events/update-now]
        (is (t/after? @now @*prev-now*))
        (is (< (:seconds @time-remaining) (:seconds @*prev-time-remaining*)))
        (reset! *prev-now* @now)
        (reset! *prev-time-remaining* @time-remaining)

        (wait-for [::events/update-now]
          (is (t/after? @now @*prev-now*))
          (is (< (:seconds @time-remaining) (:seconds @*prev-time-remaining*)))
          (reset! *prev-now* @now)
          (reset! *prev-time-remaining* @time-remaining)

          (wait-for [::events/update-now]
            (is (t/after? @now @*prev-now*))
            (is (< (:seconds @time-remaining) (:seconds @*prev-time-remaining*)))
            (reset! *prev-now* @now)

            (re-frame/dispatch-sync [::events/set-now (t/minus (t/now) (t/years 1))])
            (wait-for [::events/update-now]
              (is (= 1 (t/in-years (t/interval @now @*prev-now*))))
              (reset! *prev-now* @now)

              (re-frame/dispatch-sync [::events/increment-now (t/in-millis (t/interval @now (t/now)))])
              (wait-for [::events/update-now]
                (is (= 1 (t/in-years (t/interval @*prev-now* @now))))))))))))
