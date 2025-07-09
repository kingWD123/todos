#see https://cucumber.io/docs/gherkin/reference/
Feature:  BDD Scenarios of Todo API - Delete Todo

  Background:
    Given table todo contains data:
      | id                                   | title        | description        | completed | date_debut          | date_fin            |
      | 17a281a6-0882-4460-9d95-9c28f5852db1 | Rendre notes | Rendre notes DIC 1 | false     | 2025-08-19T12:00:00 | 2025-08-19T12:15:00 |
      | 18a81a6-0882-4460-9d95-9c28f5852db1  | Presentation | Presentation DIC 1 | false     | 2025-08-12T15:00:00 | 2030-09-02T19:00:00 |

  Scenario: Delete an existing todo should return 204
    When call delete with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the http status is 204

  Scenario: Delete an non existing todo should return 404
    When call delete with id="27a281a6-0882-4460-9d95-9c28f5852db1"
    Then the http status is 404
    And the returned error body looks like:
      | system_id                            | system_name | type       | status | message                                                        |
      | be7e84a8-9f56-405b-a15a-2646a8012c89 | MS-TODOS    | VALIDATION | 404    | Can not find todo with id=27a281a6-0882-4460-9d95-9c28f5852db1 |


