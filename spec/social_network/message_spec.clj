(ns social-network.message-spec
  (:require [speclj.core :refer :all]
            [social-network.message :refer :all])
  (:import [social_network.message Message]))

(describe "message"
  (context "make"
    (it "builds a simple message"
      (should (= (Message. "kevin" "test message" #{} #{})
                 (make "kevin" "test message"))))
    
    (it "detects a mention"
      (let [message (make "kevin" "mentions @stuart")]
        (should-contain "stuart" (:mentions message))))

    (it "detects a link"
      (let [message (make "kevin" "checkout http://github.com")]
        (should-contain "http://github.com" (:links message)))))

  (context "make-private"
    (it "creates a private message"
      (let [message (Message. "stuart" "test message" #{} #{})
            private-message (assoc message :recipient "recipent")]
      (should= (make-private "stuart", "recipent" "test message")
               private-message))))
  
  (context "public?"
    (it "confirms a message is public"
      (let [message (make "bob" "test message")]
        (should (public? message)))) 

      (it "confirms a message is not public"
        (let [message (make-private "bob" "recipient" "test message")]
          (should-not (public? message)))))
  
  (context "authored-by?"
    (let [message (make "kevin" "banana!")] [

      (it "confirms the message author is correct"
        (should (authored-by? "kevin" message)))

      (it "confirms the message author is not correct"
        (should-not (authored-by? "bob" message)))]))

  (context "from-followee?"
    (let [message (make "kevin" "banana!")] [

      (it "confirms a message is from a followee"
         (should (from-followee? #{"kevin", "bob"} message)))

      (it "confirms a message is not from a followee"
         (should-not (from-followee? #{"stuart", "bob"} message)))]))

  (context "for-recipient?"
    (let [message (make-private "kevin" "bob" "banana!")] [

      (it "confirms a message is intended for the recipient"
         (should (for-recipient? "bob" message)))

      (it "confirms a message is not not intended for the recipient"
         (should-not (for-recipient? "stuart" message)))])))   
