(ns social-network.application
  (:require [social-network.message :as message]))

(def ^:private empty-message-list '())

(def ^:private messages (atom empty-message-list))

(defn reset []
  (reset! messages empty-message-list))

(defn add-user [user])

(defn- post-message [message]
  (swap! messages #(conj % (message/make message))))

(defn- my-timeline [] @messages)

(defn- timeline-for-user [user] @messages)

(defn use-as-user [name]
  {:post-message post-message
   :my-timeline my-timeline
   :timeline-for-user timeline-for-user})
