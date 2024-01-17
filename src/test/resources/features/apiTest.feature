@api
Feature: test the add function

  Scenario: I can get the correct result via add function
    Given I provide a = 2
    # b is a customer type -- Even
    And I provide b = 6
    When I call the add function
    Then I should get the result of a + b = 8
    Then show aaa asdada end

  Scenario: I can get the correct result from json api
    When I call the json api
    Then I can see the max value in the number of winnerId:23 is 45
    And I can see the max value in the number of winnerId:54 is 52


  Scenario: I can get the correct result from json api
    When I call the xpath api
    Then I can see the quantity of "Pens" in "supplies" category is 4
    And I can see the quantity of "Pens" in "supplies2" category is 222
    And I can see "Aug 10" is "Kathryn's Birthday"
    And I can see "groceries" has "Chocolate|Coffee"