(ns twitter-client.core
	(:require [clojure.data.codec.base64 :as base64])
	(:require [clojure.data.json :as json])
	(:require [clj-http.client :as http])
	(:gen-class))

(def consumer-key "SECRET")
(def consumer-secret "SECRET")

(defn encode
	[source]
	(String. (base64/encode (.getBytes source))))

(defn authenticate
	[]
	(let [credentials (str consumer-key ":" consumer-secret)
		  authorization (str "Basic " (encode credentials))]
		(http/post "https://api.twitter.com/oauth2/token"
	             {:headers {"Authorization" authorization
	             			"Content-Type" "application/x-www-form-urlencoded"}
         		  :body "grant_type=client_credentials"})))

(defn get-bearer-token
	[]
	(let [response (authenticate)]
		(:access_token (json/read-str (:body response) :key-fn keyword))))

(defn search
	[query]
	(let [uri (str "https://api.twitter.com/1.1/search/tweets.json?count=100&q=" query)
		  bearer (get-bearer-token)]
		(http/get uri {:headers {"Authorization" (str "Bearer " bearer)}})))

(defn find-tweets
	[query]
	(let [response (search query)]
		(:statuses  (json/read-str (:body response) :key-fn keyword))))

(defn format-tweet
	[tweet]
	(str (:screen_name (:user tweet)) ": " (:text tweet)))

(defn -main
  [& args]
  (map println (map format-tweet (find-tweets "6Wunderkinder"))))
