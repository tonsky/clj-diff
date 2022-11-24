(ns build
  (:require [clojure.tools.build.api :as b]))

(def basis (b/create-basis {:project "deps.edn"}))

(defn compile-java [_]
  (println "Compiling Java")
  (b/javac {:src-dirs ["src/jvm"]
            :class-dir "target/classes"
            :javac-opts ["-source" "1.8" "-target" "1.8"]
            :basis []}))
