@browser
Feature: test the baidu search

  Background: open baidu homepage
    Given I open the browser
    And I open "baidu search" page

  Scenario: I can open the baidu search page
    Then I can see the "关于百度" in the page
#    Then I close the browser

  Scenario: I search keyword in baidu search page
    When I search "google"
    Then I can see the "google" in the page
#    Then I close the browser
