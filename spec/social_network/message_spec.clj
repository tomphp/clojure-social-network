(ns social-network.message-spec
  (:require [speclj.core :refer :all]
            [social-network.message :as message])
  (:import [social_network.message Message]))

(describe "message"
  (it "builds a simple message"
    (should (= (Message. "author" "test message" #{} #{})
               (message/make "author" "test message"))))
  
  (it "detects a mention"
    (let [message (message/make "user1" "mentions @user2")]
      (should (contains? (:mentions message) "user2"))))

  (it "detects a link"
    (let [message (message/make "user1" "checkout http://github.com")]
      (should (contains? (:links message) "http://github.com"))))) 
