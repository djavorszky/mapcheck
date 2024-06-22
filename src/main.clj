(ns main
  (:require [clojure.string :as str]
            [clj-http.client :as client]
            [clojure.data.json :as json]))

(defn read-env [env]
  (->> (str/split-lines (slurp env))
       (map #(str/split % #"\="))
       (filter #(> (count %) 1))))

(defn read-conf []
  (->> (concat (read-env ".env") (read-env ".env.dev"))
       (apply #(assoc {} (keyword (first %)) (second %)))))

(def conf (read-conf))

(def route-url "https://routes.googleapis.com/directions/v2:computeRoutes")

(def home-plid "i should really")
(def work-plid "move these two to conf")

(def headers
  {"X-Goog-Api-Key" (:MAPS_API_KEY conf),
   "X-Goog-FieldMask" "routes.duration,routes.staticDuration"})

(def ctx {:client client/post})

(defn fetch-duration [{:keys [client]} from to]
  (let [body {"origin" {"placeId" from}
              "destination" {"placeId" to}
              "travelMode" "DRIVE",
              "routingPreference" "TRAFFIC_AWARE_OPTIMAL"}
        resp (client route-url {:throw-entire-message? true
                                :content-type :json
                                :accept :json
                                :headers headers
                                :body (json/write-str body)})]
    (-> (:body resp)
        (json/read-str)
        (get "routes")
        (first)
        (get "duration"))))

(fetch-duration ctx home-plid work-plid)
(fetch-duration ctx work-plid home-plid)

(defn mock-client [& _]
  {:body (json/write-str {"routes" [{"duration" "1900s"}]})})

(fetch-duration {:client mock-client} home-plid work-plid)