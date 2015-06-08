(ns social-network.application
  (:require [social-network.message :as message]))

(def ^:private empty-message-list '())

(def ^:private messages (atom empty-message-list))

(defn reset []
  (reset! messages empty-message-list))

(defn add-user [user])

(defn- post-message [user message]
  (swap! messages #(conj % (message/make user message))))

(defn- my-timeline [] @messages)

(defn- timeline-for-user [user] @messages)

(defn- my-feed [] @messages)

(defn- follow [user])

(defn use-as-user [user]
  {:post-message (partial post-message user)
   :my-timeline my-timeline
   :timeline-for-user timeline-for-user
   :my-feed my-feed
   :follow follow})
