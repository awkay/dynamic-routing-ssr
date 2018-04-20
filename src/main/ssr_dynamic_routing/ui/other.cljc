(ns ssr-dynamic-routing.ui.other
  (:require
    #?(:cljs [cljs.loader :as loader])
    [fulcro.client.mutations :as m]
    [fulcro.client.data-fetch :as df]
    #?(:cljs [fulcro.client.dom :as dom] :clj
    [fulcro.client.dom-server :as dom])
    [ssr-dynamic-routing.api.mutations :as api]
    [fulcro.client.primitives :as prim :refer [defsc]]
    [fulcro.client.routing :as r]
    [fulcro.i18n :as i18n :refer [tr trf]]))

(defsc Other [this {:keys [x]}]
  {:query         [r/dynamic-route-key :x]
   :ident         (fn [] [:other :singleton])
   :initial-state (fn [_] {:x                  1
                           r/dynamic-route-key :other})}
  (dom/div nil (str "Other:" x)))

(defmethod r/get-dynamic-router-target :other [_] Other)
#?(:cljs (loader/set-loaded! :other))
