(ns social-network.user-spec
  (:require [speclj.core :refer :all]
            [social-network.user :refer :all]))

(describe "user"
  (context "make-from-name"
    (it "makes a user from the name",
      (should= {:name "tom", :follows #{}}
               (make-from-name "tom"))))
  
  (context "follow-user"
    (it "adds a user to be followed"
      (let [self (make-from-name "tom")]
        (should= #{"jenny"}
                 (:follows (follow-user self "jenny")))))))
