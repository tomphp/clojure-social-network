(ns social-network.atom-store-spec
  (:require [speclj.core :refer :all]
            [social-network.atom-store :refer :all]))

(def storage (atom nil))

(def instance (make-instance storage))

(describe "atom-store"
  (let [fetch-all (:fetch-all instance)
        add! (:add! instance)
        reset! (:reset! instance)] [ 

    (context "make-instance"
      (it "resets the storage"
        (should= '() (fetch-all))))

    (context "add! and fetch-all"
      (it "returns an added item"
        (add! "test item")
        (should= ["test item"] (fetch-all))))

    (context "reset!"
       (it "resets the store"
        (add! "test item")
        (reset!)
        (should= '() (fetch-all))))]))  
