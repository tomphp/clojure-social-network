(ns social-network.user-spec
  (:require [speclj.core :refer :all]
            [social-network.user :as user]))

(describe "user"
  (it "makes a user from the name",
    (should (= {:name "tom", :follows #{}}
               (user/make-from-name "tom"))))
  
  (it "adds a user to be followed"
    (let [self (user/make-from-name "tom")]
      (should (= #{"jenny"}
                 (:follows (user/follow-user self "jenny")))))))
