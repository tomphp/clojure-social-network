(require '[social-network.application :as network]
         '[social-network.user :as user]
         '[clojure.test :refer :all]) 

(def instance (atom nil))

(network/reset)

(defn- assert-equals [expected actual]
  (if (not (= expected actual))
    (let [message (str "Assert equals failed " expected " != " actual)]
      (throw (Exception. message)))))

(defn- post-message-as-user [user message]
  (let [user-instance (network/use-as-user user)
        post-message (:post-message user-instance)]
    (post-message message)))

(defn- send-private-message [from to message]
  (let [user-instance (network/use-as-user from)
        send-private-message (:send-private-message user-instance)]
    (send-private-message to message)))

(Before []
  (reset! instance nil))

(Given #"^I add user with name ([^ ]*) to the network$" [name]
  (network/add-user (user/make-from-name name)))

(Given #"^I am using the network as user ([^ ]*)$" [name]
  (reset! instance (network/use-as-user name)))

(Given #"^([^ ]*) posted a message \"([^\"]*)\"$" [user message]
  (post-message-as-user user message))

(Given #"^([^ ]*) posts a message after$" [user]
  (post-message-as-user user "random message")) 

(Given #"^I am following ([^ ]*)" [user]
    ((:follow @instance) user))

(When #"^I post a message \"([^\"]*)\"$" [message]
  ((:post-message @instance) message))

(When #"^([^ ]*) posts a message \"([^\"]*)\"$" [user message]
  (post-message-as-user user message))

(When #"^([^ ]*) sends a private message to ([^ ]*) saying \"([^\"]*)\"$"  [from to message]
  (send-private-message from to message))

(Then #"^the most recent message in my timeline should be \"([^\"]*)\"$" [message]
  (let [retrieve-timeline (:my-timeline @instance)
        found-message (take 1 (retrieve-timeline))]
    (assert-equals message (:message (first found-message)))))

(Then #"^the most recent messages on ([^\']*)'s timeline should be:$"
  [user messages]
  (let [timeline-for-user (:timeline-for-user @instance)
        expected-messages (map :message (table->rows messages))
        number-of-messages (count expected-messages)
        actual-messages (map :message
                             (take number-of-messages (timeline-for-user user)))]
    (assert-equals (seq expected-messages) (seq actual-messages))))

(Then #"my most recent message should mention ([^ ]*)$"
  [mentioned-user]
  (let [my-timeline (:my-timeline @instance)
        message (first (my-timeline))
        mentions (:mentions message)]
    (assert-equals true (contains? mentions mentioned-user))))

(Then #"^the most recent messages in my aggregated feed should be:$" [messages]
  (let [my-feed (:my-feed @instance)
        expected-messages (table->rows messages)
        number-of-messages (count expected-messages)
        actual-messages (map #(select-keys % [:message :author])
                             (take number-of-messages (my-feed)))]
    (assert-equals (seq expected-messages) (seq actual-messages))))

(Then #"^my most recent message should link to \"([^\"]*)\"$"  [link]
  (let [my-timeline (:my-timeline @instance)
        message (first (my-timeline))
        links (:links message)]
    (assert-equals true (contains? links link))))

(Then #"^I should have a message from ([^ ]*) saying \"([^\"]*)\"$"  [user message]
  (let [private-messages (:private-messages @instance)
        the-message (first (private-messages))]
    (assert-equals user (:author the-message))
    (assert-equals message (:message the-message)))
      )

