(ns social-network.message-spec
  (:require [speclj.core :refer :all]
            [social-network.message :as message]))

(describe "message"
  (it "builds a simple message"
    (should (= {:message "test message"
                :mentions #{}}
               (message/make "test message"))))
  
  (it "detects a mention"
    (let [message (message/make "mentions @user")]
      (should (contains? (:mentions message) "user")))))
