(ns ^:dev/always ssr-dynamic-routing.client-test-main
  (:require [fulcro-spec.selectors :as sel]
            [fulcro-spec.suite :as suite]))

(suite/def-test-suite client-tests {:ns-regex #"ssr-dynamic-routing.*-spec"}
  {:default   #{::sel/none :focused}
   :available #{:focused}})

(client-tests)
