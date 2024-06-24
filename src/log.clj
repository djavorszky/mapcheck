(ns log)

(defn- now [] (.toString (java.time.Instant/now)))


(defn info! [msg]
  (println (now) " - [INFO]  - " msg))

(defn warn! [msg]
  (println (now) " - [WARN]  - " msg))

(defn error! [msg]
  (println (now) " - [ERROR] - " msg))


(comment

  (info! "hello there")
  (warn! "what's up")
  (error! "oh no"))