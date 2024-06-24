(ns main
  (:gen-class)
  (:require
   [clj-http.client :as client]
   [clojure.data.json :as json]
   [config]))


(def route-url "https://routes.googleapis.com/directions/v2:computeRoutes")

(def ctx {:client client/post
          :conf (config/read-conf)})

(def headers
  {"X-Goog-Api-Key" (:MAPS_API_KEY (:conf ctx)),
   "X-Goog-FieldMask" "routes.duration,routes.staticDuration"})

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

(def conf (:conf ctx))
(def home-plid (:HOME_PLID conf))
(def work-plid (:WORK_PLID conf))

(defn mock-client [& _]
  {:body (json/write-str {"routes" [{"duration" "1900s"}]})})

(defn -main [& args]
  (println (fetch-duration ctx home-plid work-plid)))

(comment

  (fetch-duration ctx home-plid work-plid)
  (fetch-duration ctx work-plid home-plid)

  (fetch-duration {:client mock-client} home-plid work-plid))
