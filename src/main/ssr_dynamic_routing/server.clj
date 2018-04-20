(ns ssr-dynamic-routing.server
  (:require
    [ring.util.response :as resp]
    [fulcro.easy-server :refer [make-fulcro-server]]

    [fulcro.server-render :as ssr]

    ;; REQUIRE THESE: ensures all UI is loaded for the server-side rendering
    [ssr-dynamic-routing.ui.other :as other]
    [ssr-dynamic-routing.ui.root :as root]

    [fulcro.client.dom-server :as dom]

    [fulcro.client.primitives :as prim]
    [fulcro.client.routing :as r]))

(defn build-ui-tree [match]
  (let [client-db (ssr/build-initial-state (prim/get-initial-state root/Root {}) root/Root)
        final-db  (-> client-db
                    ;; CRITICAL: Install the routes, or their state won't be in the db
                    (r/install-route* :main root/Main)
                    (r/install-route* :other other/Other)
                    (r/route-to* match))]
    ;; CRITICAL: Pass the final database to get-query!!! Or you won't get the updated dynamic query
    (prim/db->tree (prim/get-query root/Root final-db) final-db final-db)))

(defn server-side-render [env {:keys [handler] :as match}]
  (let [ui-tree (build-ui-tree match)
        html    (dom/render-to-str ((prim/factory root/Root) ui-tree))]
    (-> (resp/response (str
                         "<html><body><div id='app'>"
                         html
                         "</div></body></html>"))
      (resp/content-type "text/html"))))

(defn build-server
  [{:keys [config] :or {config "config/dev.edn"}}]
  (make-fulcro-server
    :parser-injections #{:config}
    ;; Quick way to hack a couple of pages in with different match handlers
    :extra-routes {:routes   ["/" {"main.html"  :main
                                   "other.html" :other}]
                   :handlers {:main  server-side-render
                              :other server-side-render}}
    :config-path config))



