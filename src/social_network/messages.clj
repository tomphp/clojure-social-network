(ns social-network.messages
  (:require [social-network.message :refer :all]))

(defn public-only [messages] 
  (filter public? messages))

(defn authored-by [messages author-name]
  (filter (partial authored-by? author-name) messages)) 

(defn from-followees [messages followees]
  (filter (partial from-followee? followees) messages))

(defn for-recipient [messages recipient-name]
  (filter (partial for-recipient? recipient-name) messages))
