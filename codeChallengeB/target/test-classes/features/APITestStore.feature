Feature: API Tests - Store Endpoint

  Scenario: Perform place an order successfully
    Given I perform POST operation for store "/order"
    Then I should see the purchase by order id "1"

  Scenario: Perform place some orders and check inventory values updated, then delete
    Given I perform GET operation for store inventory and POST operations for store "/order" and check inventory is updated

  Scenario: Perform get an inexistent order
    Given I perform GET operation for store for an inexistent order
    Then I should see "404" on response code for Store