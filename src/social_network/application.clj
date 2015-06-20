(ns social-network.application
  (:require [social-network.message :as message]
            [social-network.user :as user]))

(def ^:private empty-message-list '())

(def ^:private messages (atom empty-message-list))
(def ^:private users (atom {}))

(defn reset [] (reset! messages empty-message-list))

(defn add-user [user] (swap! users #(assoc % (:name user) user)))

(defn- get-public-messages [] (filter message/public? @messages))

(defn- post-message [user message]
  (swap! messages #(conj % (message/make user message))))

(defn- timeline-for-user [user]
  (filter #(= user (:author %)) (get-public-messages)))

(defn- my-feed [active-user-name]
  (let [active-user (get @users active-user-name)
        follows (:follows active-user)]
    (filter #(follows (:author %)) (get-public-messages))))

(defn- follow [active-user-name user-to-follow]
  (swap! users
         #(let [active-user (get % active-user-name)]
            (assoc %
                   active-user-name
                   (user/follow-user active-user user-to-follow)))))

(defn- send-private-message [from to message]
  (swap! messages #(conj % (message/make-private from to message))))

(defn- private-messages [recipient]
  (filter #(= (:recipient %) recipient) @messages))

(defn use-as-user [user]
  {:post-message (partial post-message user)
   :my-timeline (partial timeline-for-user user)
   :timeline-for-user timeline-for-user
   :my-feed (partial my-feed user)
   :follow (partial follow user)
   :send-private-message (partial send-private-message user)
   :private-messages (partial private-messages user)})
