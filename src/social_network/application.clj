(ns social-network.application
  (:require [social-network.message :as message]
            [social-network.atom-store :as store]  
            [social-network.user-repository :as repository]  
            [social-network.user :as user]))

(def ^:private message-store (store/make-instance (atom nil)))
(def ^:private user-repository (repository/make-instance (atom nil)))

(defn reset []
  (let [reset! (:reset! message-store)]
    (reset!)))

(defn add-user [user] 
  (let [add! (:add! user-repository)]
    (add! user)))

(defn- get-public-messages [] 
  (let [fetch-all (:fetch-all message-store)
        messages (fetch-all)]
    (filter message/public? messages)))

(defn- post-message [user message]
  (let [add! (:add! message-store)
        message (message/make user message)]
    (add! message)))

(defn- timeline-for-user [user]
  (filter #(= user (:author %)) (get-public-messages)))

(defn- my-feed [active-user-name]
  (let [fetch-user-by-name (:fetch-by-name user-repository)
        active-user (fetch-user-by-name active-user-name)
        follows (:follows active-user)]
    (filter #(follows (:author %)) (get-public-messages))))

(defn- follow [active-user-name user-to-follow]
  (let [update-user! (:update! user-repository)]
    (update-user! active-user-name #(user/follow-user % user-to-follow))))

(defn- send-private-message [from to message]
  (let [add! (:add! message-store)
        message (message/make-private from to message)]
    (add! message)))

(defn- private-messages [recipient]
  (let [fetch-all (:fetch-all message-store)
        messages (fetch-all)
        recipient-filter #(= (:recipient %) recipient)]
  (filter recipient-filter messages)))

(defn use-as-user [user]
  {:post-message (partial post-message user)
   :my-timeline (partial timeline-for-user user)
   :timeline-for-user timeline-for-user
   :my-feed (partial my-feed user)
   :follow (partial follow user)
   :send-private-message (partial send-private-message user)
   :private-messages (partial private-messages user)})
