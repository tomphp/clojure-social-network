(ns social-network.message)

(defrecord Message [author message mentions])

(defn make [author message]
  (let [mention-matches (re-seq #"@([A-Za-z0-9]*)" message)
        mentions (map second mention-matches)]
    (Message. author message (set mentions))))
