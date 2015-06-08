(require '[social-network.application :as network]
         '[social-network.user :as user]
         '[clojure.test :refer :all]) 

(def instance (atom nil))
(network/reset)

(defn assert-equals [expected actual]
  (if (not (= expected actual))
    (let [message (str "Assert equals failed " expected " != " actual)]
      (throw (Exception. message)))))

(Before []
  (reset! instance nil))

(Given #"^I add user with name ([^ ]*) to the network$" [name]
  (network/add-user (user/make-from-name name)))

(Given #"^I am using the network as user ([^ ]*)$" [name]
  (reset! instance (network/use-as-user name)))

(Given #"^([^ ]*) posted a message \"([^\"]*)\"$" [user message]
  (let [user-instance (network/use-as-user user)]
    ((:post-message user-instance) message)))

(When #"^I post a message \"([^\"]*)\"$"  [message]
  ((:post-message @instance) message))

(Then #"^\"([^\"]*)\" should be in my timeline$" [message]
  (let [retrieve-timeline (:my-timeline @instance)
        found-message (take 1 (retrieve-timeline))]
    (assert-equals message (:message (first found-message)))))

(Then #"^the most recent messages on ([^\']*)'s timeline should be:$"
  [user messages]
  (let [timeline-for-user (:timeline-for-user @instance)
        expected-messages (map :message (table->rows messages))
        number-of-messages (count expected-messages)
        actual-messages (map :message (take number-of-messages (timeline-for-user user)))]
    (assert-equals (seq expected-messages) (seq actual-messages))))

(Then #"my most recent message should mention ([^ ]*)$"
  [mentioned-user]
  (let [my-timeline (:my-timeline @instance)
        message (first (my-timeline))
        mentions (:mentions message)]
    (assert-equals true (contains? mentions mentioned-user))))
