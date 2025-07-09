#see https://cucumber.io/docs/gherkin/reference/
Feature:  BDD Scenarios of Todo API - Update Todo

  Background:
    Given table todo contains data:
      | id                                   | title        | description        | completed | date_debut          | date_fin            |
      | 17a281a6-0882-4460-9d95-9c28f5852db1 | Rendre notes | Rendre notes DIC 1 | false     | 2025-08-19T12:00:00 | 2025-08-19T12:15:00 |
      | 18a81a6-0882-4460-9d95-9c28f5852db1  | Presentation | Presentation DIC 1 | false     | 2025-08-12T15:00:00 | 2030-09-02T19:00:00 |

  Scenario: Update an existing todo should return 202
    And the following todo to update:
      | title               | description             | date_debut          | date_fin            |
      | Rendre notes projet | Rendre notes DIC projet | 2025-08-19T12:15:00 | 2025-08-19T12:30:00 |
    When call update todo with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the http status is 202
    And the returned todo has following properties:
      | title               | description             | completed | date_debut          | date_fin            |
      | Rendre notes projet | Rendre notes DIC projet | false     | 2025-08-19T12:15:00 | 2025-08-19T12:30:00 |

  Scenario: Update an non existing todo should return 404
    And the following todo to update:
      | title               | description             | date_debut          | date_fin            |
      | Rendre notes projet | Rendre notes DIC projet | 2025-08-19T12:15:00 | 2025-08-19T12:30:00 |
    When call update todo with id="27a281a6-0882-4460-9d95-9c28f5852db1"
    Then the http status is 404
    And the returned error body looks like:
      | system_id                            | system_name | type       | status | message                                                        |
      | be7e84a8-9f56-405b-a15a-2646a8012c89 | MS-TODOS    | VALIDATION | 404    | Can not find todo with id=27a281a6-0882-4460-9d95-9c28f5852db1 |


  Scenario Outline: Update todo with title not matching size constraintes should return 400
    And the following todo to update:
      | title   | description             | date_debut          | date_fin            |
      | <title> | Rendre notes DIC projet | 2025-08-19T12:15:00 | 2025-08-19T12:30:00 |
    When call update todo with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the http status is 400
    And the returned error body looks like:
      | system_id                            | system_name | type       | status | message                                          |
      | be7e84a8-9f56-405b-a15a-2646a8012c89 | MS-TODOS    | VALIDATION | 400    | title: title must be between 2 and 80 characters |
    Examples:
      | title                                                                                          |
      | t                                                                                              |
      | Review and finalize the quarterly financial report before submission to the board of directors |

  Scenario: Update todo with null title should return 400
    And the following todo to update:
      | title | description             | date_debut          | date_fin            |
      | null  | Rendre notes DIC projet | 2025-08-19T12:15:00 | 2025-08-19T12:30:00 |
    When call update todo with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the http status is 400
    And the returned error body looks like:
      | system_id                            | system_name | type       | status | message                  |
      | be7e84a8-9f56-405b-a15a-2646a8012c89 | MS-TODOS    | VALIDATION | 400    | title: title is required |

  Scenario Outline: Update todo with description not matching size constraintes should return 400
    And the following todo to update:
      | title        | description   | date_debut          | date_fin            |
      | Rendre notes | <description> | 2025-08-19T12:15:00 | 2025-08-19T12:30:00 |
    When call update todo with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the http status is 400
    And the returned error body looks like:
      | system_id                            | system_name | type       | status | message                                                 |
      | be7e84a8-9f56-405b-a15a-2646a8012c89 | MS-TODOS    | VALIDATION | 400    | description: description must not exceed 255 characters |
    Examples:
      | description                                                                                                                                                                                                                                                                                                               |
      | This task involves conducting a comprehensive code review of the entire microservices architecture to ensure consistency, scalability, and adherence to established design principles. All findings should be documented clearly, and any security vulnerabilities must be flagged for immediate resolution and follow-up |


#  dateDebut is required
  Scenario: Update an existing todo with dateDebut null should return 400
    And the following todo to update:
      | title               | description             | date_debut | date_fin            |
      | Rendre notes projet | Rendre notes DIC projet | null       | 2025-08-19T12:30:00 |
    When call update todo with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the http status is 400
    And the returned error body looks like:
      | system_id                            | system_name | type       | status | message                          |
      | be7e84a8-9f56-405b-a15a-2646a8012c89 | MS-TODOS    | VALIDATION | 400    | dateDebut: dateDebut is required |

#  dateDebut must be in the future
  Scenario: Update an existing todo with dateFin null should return 400
    And the following todo to update:
      | title               | description             | date_debut          | date_fin |
      | Rendre notes projet | Rendre notes DIC projet | 2025-08-19T12:15:00 | null     |
    When call update todo with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the http status is 400
    And the returned error body looks like:
      | system_id                            | system_name | type       | status | message                      |
      | be7e84a8-9f56-405b-a15a-2646a8012c89 | MS-TODOS    | VALIDATION | 400    | dateFin: dateFin is required |


  Scenario: Update an existing todo with dateDebut in the pass should return 400
    And the following todo to update:
      | title               | description             | date_debut          | date_fin            |
      | Rendre notes projet | Rendre notes DIC projet | 1900-08-19T12:15:00 | 2025-08-19T12:30:00 |
    When call update todo with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the http status is 400
    And the returned error body looks like:
      | system_id                            | system_name | type       | status | message                                    |
      | be7e84a8-9f56-405b-a15a-2646a8012c89 | MS-TODOS    | VALIDATION | 400    | dateDebut: dateDebut must be in the future |


  Scenario: Update an existing todo with dateFin in the pass should return 400
    And the following todo to update:
      | title               | description             | date_debut          | date_fin            |
      | Rendre notes projet | Rendre notes DIC projet | 2025-08-19T12:15:00 | 1900-08-19T12:30:00 |
    When call update todo with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the http status is 400
    And the returned error body looks like:
      | system_id                            | system_name | type       | status | message                                |
      | be7e84a8-9f56-405b-a15a-2646a8012c89 | MS-TODOS    | VALIDATION | 400    | dateFin: dateFin must be in the future |

  Scenario: Update an existing todo with dateDebut after dateFin should return 400
    And the following todo to update:
      | title               | description             | date_debut          | date_fin            |
      | Rendre notes projet | Rendre notes DIC projet | 2025-08-19T12:15:00 | 2025-08-19T12:00:00 |
    When call update todo with id="17a281a6-0882-4460-9d95-9c28f5852db1"
    Then the http status is 400
    And the returned error body looks like:
      | system_id                            | system_name | type       | status | message                                  |
      | be7e84a8-9f56-405b-a15a-2646a8012c89 | MS-TODOS    | VALIDATION | 400    | Field dateDebut should be before dateFin |

