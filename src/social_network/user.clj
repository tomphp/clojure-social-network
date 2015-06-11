(ns social-network.user)

(defn make-from-name [name] {:name name, :follows #{}})

(defn follow-user [self user]
  (let [follows (:follows self)]
    (assoc self :follows (conj follows user))))
