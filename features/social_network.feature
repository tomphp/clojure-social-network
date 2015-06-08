Feature: Social network
  In order to investigate the functional calisthenics proposed at Socrates UK 2015
  As an aspiring craftsman
  I must complete the Social Network Kata

  Background:
    Given I add user with name Alice to the network
    And I add user with name Bob to the network
    And I add user with name Charlie to the network
    And I add user with name Mallory to the network

  # Test is only alices timeline
  Scenario: Alice can post messages to her personal timeline
    Given I am using the network as user Alice
    When I post a message "Looking forward to breakfast"
    Then "Looking forward to breakfast" should be in my timeline

  # Test is only Alice's timeline
  Scenario: Bob can view Alice's timeline
    Given Alice posted a message "good morning"
    And Alice posted a message "good day"
    And I am using the network as user Bob
    Then the most recent messages on Alice's timeline should be:
      | message      |
      | good day     |
      | good morning |

  Scenario: Bob mentions Charlie
    Given I am using the network as user Bob
    When I post a message "hi @Charlie"
    Then my most recent message should mention Charlie

  Scenario: Alice posts a message containing a link
    Given I am using the nework as Alice
    When I post a message "Searching with http://www.google.com"
    Then my most recent message should link to "http://www.google.com"

# Scenario: Mallory sends a private message to Alice
#   Given I am using the network as user Alice
#   When Mallory sends a private message to me saying "Hi Alice"
#   Then I should have a message from Mallory saying "Hi Alice"
