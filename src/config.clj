(ns config
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [utils :refer [arr->map]]))

(defn- discover-confs [known-confs]
  (let [confs (->> (.listFiles (io/file "."))
                   (filter #(.isFile %))
                   (map #(.getName %))
                   (into #{}))]
    (filter #(contains? confs %) known-confs)))

(defn- read-env [env]
  (->> (str/split-lines (slurp env))
       (map #(str/split % #"\="))
       (filter #(> (count %) 1))))

(defn read-conf []
  (->> (discover-confs [".env.prod" ".env.dev" ".env"])
       (map read-env)
       (apply concat)
       (reduce arr->map {})))
