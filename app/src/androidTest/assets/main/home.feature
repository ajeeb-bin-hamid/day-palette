Feature: Home screen
  As a user I want to see a list of future holidays for the selected country on the home screen.

  Scenario: Displaying holidays on the home screen
    Given I am on the home screen
    Then I should see the title displayed at the top of the home screen
    And I should see the list of future holidays on the home screen
    And Each holiday on the home screen should display date and name