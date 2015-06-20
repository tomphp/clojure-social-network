(ns social-network.message-spec
  (:require [speclj.core :refer :all]
            [social-network.message :refer :all])
  (:import [social_network.message Message]))

(describe "message"
  (context "make"
    (it "builds a simple message"
      (should (= (Message. "author" "test message" #{} #{})
                 (make "author" "test message"))))
    
    (it "detects a mention"
      (let [message (make "user1" "mentions @user2")]
        (should-contain "user2" (:mentions message))))

    (it "detects a link"
      (let [message (make "user1" "checkout http://github.com")]
        (should-contain "http://github.com" (:links message)))))

  (context "make-private"
    (it "creates a private message"
      (let [message (Message. "author" "test message" #{} #{})
            private-message (assoc message :recipient "recipent")]
      (should= (make-private "author", "recipent" "test message")
               private-message))))
  
  (context "public?"
    (it "confirms a message is public"
      (let [message (make "author" "test message")]
        (should (public? message))))) 

    (it "confirms a message is not public"
      (let [message (make-private "author" "recipient" "test message")]
        (should-not (public? message)))))   
