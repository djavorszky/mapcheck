(ns utils)

(defn arr->map [acc [k v]] (assoc acc (keyword k) v))