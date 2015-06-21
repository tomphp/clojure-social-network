(ns social-network.atom-store)

(defn- add [storage item] (swap! storage #(conj % item)))

(defn- fetch-all [storage] @storage)

(defn- reset [storage] (reset! storage '()))

(defn make-instance [storage]
  (reset storage)
  {:add! (partial add storage)
   :fetch-all (partial fetch-all storage)
   :reset! (partial reset storage)})
