(ns social-network.user-repository-spec
  (:require [speclj.core :refer :all]
            [social-network.user-repository :refer :all]
            [social-network.user :as user]))  

(def ^:private instance (make-instance (atom nil)))

(describe "user repository"
  (let [add! (:add! instance)
        fetch-by-name (:fetch-by-name instance)
        update! (:update! instance)] [

    (context "get-user-by-name"
      (it "it gets a user by it's name"
        (let [user (user/make-from-name "kevin")]
          (add! user)
          (should= user (fetch-by-name "kevin")))))
    
    (context "update!"
      (it "updates a user by name"
        (let [user (user/make-from-name "bob")]
          (add! user)
          (update! "bob" #(assoc % :follows #{"stuart"})) 
          (should= #{"stuart"} (:follows (fetch-by-name "bob"))))))]))
