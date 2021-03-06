(require '[social-network.application :as application]
         '[social-network.user :as user]
         '[social-network.atom-store :as store]  
         '[social-network.user-repository :as repository]  
         '[speclj.core :refer :all]) 

(def ^:private message-store (store/make-instance (atom nil)))
(def ^:private user-repository (repository/make-instance (atom nil)))
(def ^:private network (application/make-instance user-repository message-store))

(def ^:private instance (atom nil))

(defn- post-message-as-user [user message]
  (let [user-instance ((:use-as-user network) user)
        post-message (:post-message user-instance)]
    (post-message message)))

(defn- send-private-message [from to message]
  (let [user-instance ((:use-as-user network) from)
        send-private-message (:send-private-message user-instance)]
    (send-private-message to message)))

(Before []
  ((:reset! message-store)))

(Given #"^I add user with name ([^ ]*) to the network$" [name]
  ((:add-user network) (user/make-from-name name)))

(Given #"^I am using the network as user ([^ ]*)$" [name]
  (reset! instance ((:use-as-user network) name)))

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
    (should= message (:message (first found-message)))))

(Then #"^the most recent messages on ([^\']*)'s timeline should be:$"
  [user messages]
  (let [timeline-for-user (:timeline-for-user @instance)
        expected-messages (map :message (table->rows messages))
        number-of-messages (count expected-messages)
        actual-messages (map :message
                             (take number-of-messages (timeline-for-user user)))]
    (should= (seq expected-messages) (seq actual-messages))))

(Then #"my most recent message should mention ([^ ]*)$"
  [mentioned-user]
  (let [my-timeline (:my-timeline @instance)
        message (first (my-timeline))
        mentions (:mentions message)]
    (should (contains? mentions mentioned-user))))

(Then #"^the most recent messages in my aggregated feed should be:$" [messages]
  (let [my-feed (:my-feed @instance)
        expected-messages (table->rows messages)
        number-of-messages (count expected-messages)
        actual-messages (map #(select-keys % [:message :author])
                             (take number-of-messages (my-feed)))]
    (should= (seq expected-messages) (seq actual-messages))))

(Then #"^my most recent message should link to \"([^\"]*)\"$"  [link]
  (let [my-timeline (:my-timeline @instance)
        message (first (my-timeline))
        links (:links message)]
    (should (contains? links link))))

(Then #"^I should have a message from ([^ ]*) saying \"([^\"]*)\"$"  [user message]
  (let [private-messages (:private-messages @instance)
        the-message (first (private-messages))]
    (should= user (:author the-message))
    (should= message (:message the-message))))
