(ns ssr-dynamic-routing.ui.root
  (:require
    [fulcro.client.mutations :as m]
    [fulcro.client.data-fetch :as df]
    #?(:cljs [cljs.loader :as loader])
    #?(:cljs [fulcro.client.dom :as dom] :clj
    [fulcro.client.dom-server :as dom])
    [ssr-dynamic-routing.api.mutations :as api]
    [fulcro.client.primitives :as prim :refer [defsc]]
    [fulcro.client.routing :as r]
    [fulcro.i18n :as i18n :refer [tr trf]]))

(def routing-tree (r/routing-tree
                    (r/make-route :main [(r/router-instruction :top-router [:main :singleton])])
                    (r/make-route :other [(r/router-instruction :top-router [:other :singleton])])))

(defsc Main [this {:keys [y]}]
  {:query         [r/dynamic-route-key :y]
   :initial-state (fn [_] {r/dynamic-route-key :main
                           :y                  3})
   :ident         (fn [] [:main :singleton])}
  (dom/div (str "MAIN " y)))

(def ui-main (prim/factory Main))

(defsc Root [this {:keys [top-router]}]
  {:query         [{:top-router (r/get-dynamic-router-query :top-router)}]
   :initial-state (fn [_] (merge
                            routing-tree
                            {r/dynamic-route-key :main
                             :top-router         (prim/get-initial-state r/DynamicRouter {:id :top-router})}))}
  (r/ui-dynamic-router top-router))

(defmethod r/get-dynamic-router-target :main [_] Main)
#?(:cljs (loader/set-loaded! :main))
