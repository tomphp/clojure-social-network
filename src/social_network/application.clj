(ns social-network.application
  (:require [social-network.message :as message]
            [social-network.user :as user]))

(defn- add-user [user-repository user] 
  (let [add! (:add! user-repository)]
    (add! user)))

(defn- get-public-messages [message-store] 
  (let [fetch-all (:fetch-all message-store)
        messages (fetch-all)]
    (filter message/public? messages)))

(defn- post-message [message-store user message]
  (let [add! (:add! message-store)
        message (message/make user message)]
    (add! message)))

(defn- timeline-for-user [message-store user]
  (filter #(= user (:author %)) (get-public-messages message-store)))

(defn- my-feed [user-repository message-store active-user-name]
  (let [fetch-user-by-name (:fetch-by-name user-repository)
        active-user (fetch-user-by-name active-user-name)
        follows (:follows active-user)]
    (filter #(follows (:author %)) (get-public-messages message-store))))

(defn- follow [user-repository active-user-name user-to-follow]
  (let [update-user! (:update! user-repository)]
    (update-user! active-user-name #(user/follow-user % user-to-follow))))

(defn- send-private-message [message-store from to message]
  (let [add! (:add! message-store)
        message (message/make-private from to message)]
    (add! message)))

(defn- private-messages [message-store recipient]
  (let [fetch-all (:fetch-all message-store)
        messages (fetch-all)
        recipient-filter #(= (:recipient %) recipient)]
  (filter recipient-filter messages)))

(defn use-as-user [user-repository message-store user]
  {:post-message (partial post-message message-store user)
   :my-timeline (partial timeline-for-user message-store user)
   :timeline-for-user (partial timeline-for-user message-store)
   :my-feed (partial my-feed user-repository message-store user)
   :follow (partial follow user-repository user)
   :send-private-message (partial send-private-message message-store  user)
   :private-messages (partial private-messages message-store user)})

(defn make-instance [user-repository message-store]
  {:add-user (partial add-user user-repository)
   :use-as-user (partial use-as-user user-repository message-store)})
