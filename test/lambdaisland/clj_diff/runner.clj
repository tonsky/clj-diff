(ns lambdaisland.clj-diff.runner
  "Test runner for babashka, until kaocha works with bb :)"
  (:require [clojure.test :as t]))

(defn run-tests [_]
  (let [test-nss '[lambdaisland.clj-diff.core-test
                   lambdaisland.clj-diff.miller-test]]
    (doseq [test-ns test-nss]
      (require test-ns))
    (let [{:keys [fail error]}
          (apply t/run-tests test-nss)]
      (when (and fail error (pos? (+ fail error)))
        (throw (ex-info "Tests failed" {:babashka/exit 1}))))))
