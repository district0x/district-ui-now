# district-ui-now

[![Build Status](https://travis-ci.org/district0x/district-ui-now.svg?branch=master)](https://travis-ci.org/district0x/district-ui-now)

Clojurescript [re-mount](https://github.com/district0x/d0x-INFRA/blob/master/re-mount.md) module, that provides current time.
Useful when you need to display time remaining counter on the website (e.g for auctions). This module uses [cljs-time](https://github.com/andrewmcveigh/cljs-time).

## Installation
Add `[district0x/district-ui-now "1.0.2"]` into your project.clj
Include `[district.ui.now]` in your CLJS file, where you use `mount/start`

## API Overview

**Warning:** district0x modules are still in early stages, therefore API can change in a future.

- [district.ui.now](#districtuinow)
- [district.ui.now.subs](#districtuinowsubs)
  - [::now](#now-sub)
  - [::time-remaining](#time-remaining-sub)
- [district.ui.now.events](#districtuinowevents)
  - [::update-now](#update-now)
  - [::set-now](#set-now)
  - [::increment-now](#increment-now)
- [district.ui.now.queries](#districtuinowqueries)
  - [now](#now)
  - [time-remaining](#time-remaining)
  - [assoc-now](#assoc-now)

## district.ui.now
This namespace contains now [mount](https://github.com/tolitius/mount) module.
This module has no configuration parameters.

```clojure
  (ns my-district.core
    (:require [mount.core :as mount]
              [district.ui.now]))

  (-> (mount/with-args
        {})
    (mount/start))
```

## district.ui.now.subs
re-frame subscriptions provided by this module:

#### <a name="now-sub">`::now []`
Returns cljs-time time of now.

#### <a name="time-remaining-sub">`::time-remaining [to-time]`
Returns time remaining from now to `to-time`.

```clojure
(ns my-district.core
    (:require [mount.core :as mount]
              [district.ui.now.subs :as now-subs]
              [cljs-time.core :as t]))

  (defn home-page []
    (let [time-remaining (subscribe [::now-subs/time-remaining (t/plus (t/now) (t/seconds 50))])]
      (fn []
        [:div "Seconds remaining: " (:seconds @time-remaining)])))
```

## district.ui.now.events
re-frame events provided by this module:

#### <a name="update-now">`::update-now`
Event fired every second to update now in re-frame db.

#### <a name="set-now">`::set-now`
Event to set now time in re-frame db, from which time incrementing continues.

```clojure
(ns my-district.core
    (:require [district.ui.now.events :as now-events]
              [cljs-time.core :as t]
              [re-frame.core :as re-frame]))

(re-frame/dispatch [::now-events/set-now (t/minus (t/now) (t/years 1))])
```

#### <a name="increment-now">`::increment-now`
Event to increment now time in re-frame db by a number of milliseconds.

```clojure
(ns my-district.core
    (:require [district.ui.now.events :as now-events]
              [re-frame.core :as re-frame]))

(re-frame/dispatch [::now-events/increment-now 8.64e+7])
```

## district.ui.now.queries
DB queries provided by this module:
*You should use them in your events, instead of trying to get this module's
data directly with `get-in` into re-frame db.*

#### <a name="now">`now [db]`
Works the same way as sub `::now`.

#### <a name="time-remaining">`time-remaining [db to-time]`
Works the same way as sub `::time-remaining`.

#### <a name="assoc-now">`assoc-now [db now]`
Associates new now and returns new re-frame db.

## Development
```bash
lein deps

# To run tests and rerun on changes
lein doo chrome tests
```
