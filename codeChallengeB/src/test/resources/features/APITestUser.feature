Feature: API Tests - User Endpoint

  Scenario: Perform log in successfully
    Given I perform GET operation for user "/login"
    Then I should see "200" on response code for User
    And I should see the user session on response

  Scenario: Perform log out successfully
    Given I perform GET operation for user "/logout"
    Then I should see "200" on response code for User
    And I should see the logout message

  Scenario: Create user and check by username
    Given I perform POST operation for user creation username "testauto"
    Then I should see the username "testauto" created

  Scenario: Create user from list and check by username
    Given I perform POST operation for users list creation usernames "testauto;testauto2;testauto3"
    Then I should see the list of users "testauto;testauto2;testauto3" created

  Scenario: Update user data and check by username
    Given I perform POST operation for user creation username "testautoforupdate"
    Then I should see the username "testautoforupdate" created
    Given I perform PUT operation for user update username "testautoforupdate"
    And I should see the username "testautoforupdate" updated

  Scenario: Delete user data and check by username
    Given I perform POST operation for user creation username "testautofordelete"
    Then I should see the username "testautofordelete" created
    Given I perform DELETE operation for user deletion username "testautofordelete"
    Then I should see the username "testautofordelete" deleted