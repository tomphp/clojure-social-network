(ns social-network.application
  (:require [social-network.message :as message]
            [social-network.user :as user]))

(defn- add-user [user-repository user] 
  (let [add! (:add! user-repository)]
    (add! user)))

(defn- public-only [messages] 
  (filter message/public? messages))

(defn- post-message [message-store user message]
  (let [add! (:add! message-store)
        message (message/make user message)]
    (add! message)))

(defn- timeline-for-user [fetch-messages user]
  (filter #(= user (:author %)) (public-only (fetch-messages))))

(defn- my-feed [user-repository fetch-messages active-user-name]
  (let [fetch-user-by-name (:fetch-by-name user-repository)
        active-user (fetch-user-by-name active-user-name)
        follows (:follows active-user)]
    (filter #(follows (:author %)) (public-only (fetch-messages)))))

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

(defn use-as-user [user-repository message-store user-name]
  (let [fetch-messages (:fetch-all message-store)
        fetch-user-by-name (:fetch-by-name user-repository)
        active-user (fetch-user-by-name user-name)]
    {:post-message (partial post-message message-store user-name)
     :my-timeline (partial timeline-for-user fetch-messages user-name)
     :timeline-for-user (partial timeline-for-user fetch-messages)
     :my-feed (partial my-feed user-repository fetch-messages user-name)
     :follow (partial follow user-repository user-name)
     :send-private-message (partial send-private-message message-store  user-name)
     :private-messages (partial private-messages message-store user-name)}))

(defn make-instance [user-repository message-store]
  {:add-user (partial add-user user-repository)
   :use-as-user (partial use-as-user user-repository message-store)})
