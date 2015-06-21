(ns social-network.user-repository)

(defn- add! [users user] 
  (swap! users #(assoc % (:name user) user)) )

(defn- fetch-by-name [users name]
  (get @users name))

(defn- update! [users name update-action]
  (let [user (fetch-by-name users name)]
    (swap! users #(assoc % name (update-action user)))))

(defn make-instance [users]
  (reset! users {})
  {:add! (partial add! users)
   :fetch-by-name (partial fetch-by-name users)
   :update! (partial update! users)})
