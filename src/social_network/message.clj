(ns social-network.message)

(defn make [message]
  (let [mention-matches (re-seq #"@([A-Za-z0-9]*)" message)
        mentions (map second mention-matches)]
    {:message message
     :mentions (set mentions)}))
