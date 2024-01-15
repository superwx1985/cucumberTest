Feature: test the add function

  Scenario: I can get the correct result via add function
    Given I provide a = 2
    And I provide b = 6
    When I call the add function
    Then I should get the result of a + b = 8
