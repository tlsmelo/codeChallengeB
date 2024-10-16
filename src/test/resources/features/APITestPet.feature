Feature: API Tests - Pet Endpoint

  Scenario: Perform add a pet successfully
    Given I perform POST operation for pet
    Then I should see the pet by id "11"

  Scenario: Perform add a pet, then update it and check success
    Given I perform POST operation for pet
    Then I should see the pet by id "11"
    Given I perform PUT operation for pet "11" update and check

  Scenario: Perform add a pet, then update by post and check success
    Given I perform POST operation for pet
    Then I should see the pet by id "11"
    Given I perform POST operation for pet "11" update and check

  Scenario: Perform add a pet, then do a find by status
    Given I perform POST operation for pet with status "pending"
    Then I perform GET operation for pet by status "pending" and check

  Scenario: Perform add a pet, then do a find by tag
    Given I perform POST operation for pet with tag "tag5a"
    Then I perform GET operation for pet by tag "tag5a" and check

  #Ignore this test for now
#  Scenario: Perform add a pet and then add an image
#    Given I perform POST operation for pet
#    Then I perform POST operation uploading an image for pet "11"