(ns social-network.application
  (:require [social-network.message :as message]))

(def ^:private empty-message-list '())

(def ^:private messages (atom empty-message-list))

(defn reset []
  (reset! messages empty-message-list))

(defn add-user [user])

(defn- post-message [user message]
  (swap! messages #(conj % (message/make user message))))

(defn- timeline-for-user [user] (filter #(= user (:author %)) @messages))

(defn- my-feed [] @messages)

(defn- follow [user])

(defn use-as-user [user]
  {:post-message (partial post-message user)
   :my-timeline (partial timeline-for-user user)
   :timeline-for-user timeline-for-user
   :my-feed my-feed
   :follow follow})
