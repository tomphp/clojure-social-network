(ns social-network.application
  (:require [social-network.message :as message]
            [social-network.messages :as messages]
            [social-network.user :as user]))

(defn- add-user [user-repository user] 
  (let [add! (:add! user-repository)]
    (add! user)))

(defn- post-message [message-store user message]
  (let [add! (:add! message-store)
        message (message/make user message)]
    (add! message)))

(defn- send-private-message [message-store from to message]
  (let [add! (:add! message-store)
        message (message/make-private from to message)]
    (add! message)))

(defn- follow [user-repository active-user-name user-to-follow]
  (let [update-user! (:update! user-repository)]
    (update-user! active-user-name #(user/follow-user % user-to-follow))))

(defn- timeline-for-user [fetch-messages user-name]
  (-> (fetch-messages)
      messages/public-only
      (messages/authored-by user-name)))

(defn- my-feed [fetch-active-user fetch-messages]
  (let [active-user (fetch-active-user)
        followees (:follows active-user)]
    (-> (fetch-messages)
        messages/public-only
        (messages/from-followees followees))))

(defn- private-messages [fetch-messages recipient-name]
  (-> (fetch-messages) (messages/for-recipient recipient-name)))

(defn use-as-user [user-repository message-store user-name]
  (let [fetch-messages (:fetch-all message-store)
        fetch-user-by-name (:fetch-by-name user-repository)
        fetch-active-user (partial fetch-user-by-name user-name)]
    {:post-message (partial post-message message-store user-name)
     :my-timeline (partial timeline-for-user fetch-messages user-name)
     :timeline-for-user (partial timeline-for-user fetch-messages)
     :my-feed (partial my-feed fetch-active-user fetch-messages)
     :follow (partial follow user-repository user-name)
     :send-private-message (partial send-private-message message-store user-name)
     :private-messages (partial private-messages fetch-messages user-name)}))

(defn make-instance [user-repository message-store]
  {:add-user (partial add-user user-repository)
   :use-as-user (partial use-as-user user-repository message-store)})
