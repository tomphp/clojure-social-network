(ns social-network.message)

(defrecord Message [author message mentions links])

(defn make [author message]
  (let [mention-matches (re-seq #"@([A-Za-z0-9]*)" message)
        mentions (map second mention-matches)
        link-matches (re-seq #"(http://[^\s]*)" message)
        links (map second link-matches)]
    (Message. author message (set mentions) (set links))))

(defn make-private [from to message]
  (assoc (make from message) :recipient to))

(defn public? [message]
  ((comp not contains?) message :recipient))

(defn authored-by? [author-name message]
  (= (:author message) author-name))

(defn from-followee? [followees message]
  (contains? followees (:author message)))

(defn for-recipient? [recipient-name message]
  (= (:recipient message) recipient-name))
