(ns social-network.messages-spec
  (:require [speclj.core :refer :all]
            [social-network.messages :refer :all]
            [social-network.message :as message]))

(describe "messages"
 ( let [kevin-public (message/make "kevin" "banana!")
        bob-private (message/make-private "bob" "stuart" "kevin is stupid")
        stuart-public (message/make "stuart" "BANANA!!!")
        stuart-private (message/make-private "stuart" "bob" "very stupid")
        bob-public (message/make "bob" "BANANANANA!!!")
        messages [kevin-public
                  bob-private
                  stuart-public
                  bob-public]] [

  (context "public-only"
    (it "filters out non public messages"
      (should= [kevin-public stuart-public bob-public]
               (public-only messages))))
    
  (context "authored-by"
    (it "filters out messages by other authors"
      (should= [kevin-public] (authored-by messages "kevin"))))
  
  (context "from-followees"
    (it "filters out messages from users not in the followee set"
      (should= [kevin-public stuart-public]
               (from-followees messages #{"kevin" "stuart"}))))
  
  (context "for-recipient"
    (it "filters out messages which are not for the recipient"
      (should= [bob-private] (for-recipient messages "stuart"))))]))
