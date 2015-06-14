(ns social-network.message-spec
  (:require [speclj.core :refer :all]
            [social-network.message :as message])
  (:import [social_network.message Message]))

(describe "message"
  (describe "make"
    (it "builds a simple message"
      (should (= (Message. "author" "test message" #{} #{})
                 (message/make "author" "test message"))))
    
    (it "detects a mention"
      (let [message (message/make "user1" "mentions @user2")]
        (should (contains? (:mentions message) "user2"))))

    (it "detects a link"
      (let [message (message/make "user1" "checkout http://github.com")]
        (should (contains? (:links message) "http://github.com")))))

  (describe "make-private"
    (it "creates a private message"
      (let [message (Message. "author" "test message" #{} #{})
            private-message (assoc message :recipient "recipent")]
      (should (= private-message
                 (message/make-private "author", "recipent" "test message"))))))
  
  (describe "is-public"
    (it "confirms a message is public"
      (let [message (message/make "author" "test message")]
        (should (= true (message/is-public message)))))) 

    (it "confirms a message is not public"
      (let [message (message/make-private "author" "recipient" "test message")]
        (should (= false (message/is-public message))))))   
