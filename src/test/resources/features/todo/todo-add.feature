#see https://cucumber.io/docs/gherkin/reference/
Feature:  BDD Scenarios of Todo API - Add Todo

  Background:
    Given table todo contains data:
      | id | title | description | completed | date_debut | date_fin |
#      | 17a281a6-0882-4460-9d95-9c28f5852db1 | Rendre notes | Rendre notes DIC 1 | false     | 2025-08-19T12:00:00 | 2025-08-19T12:15:00 |
#      | 18a81a6-0882-4460-9d95-9c28f5852db1  | Presentation | Presentation DIC 1 | false     | 2025-08-12T15:00:00 | 2030-09-02T19:00:00 |

  Scenario: AAdd todo should return 200
    And the following todo to add:
      | title               | description             | date_debut          | date_fin            |
      | Rendre notes projet | Rendre notes DIC projet | 2025-08-19T12:15:00 | 2025-08-19T12:30:00 |
    When call add todo
    Then the http status is 201
    And the returned todo has following properties:
      | title               | description             | completed | date_debut          | date_fin            |
      | Rendre notes projet | Rendre notes DIC projet | false     | 2025-08-19T12:15:00 | 2025-08-19T12:30:00 |

  Scenario Outline: AAdd todo with title not matching size constraintes should return 400
    And the following todo to add:
      | title   | description             | date_debut          | date_fin            |
      | <title> | Rendre notes DIC projet | 2025-08-19T12:15:00 | 2025-08-19T12:30:00 |
    When call add todo
    Then the http status is 400
    And the returned error body looks like:
      | system_id                            | system_name | type       | status | message                                          |
      | be7e84a8-9f56-405b-a15a-2646a8012c89 | MS-TODOS    | VALIDATION | 400    | title: title must be between 2 and 80 characters |
    Examples:
      | title                                                                                          |
      | t                                                                                              |
      | Review and finalize the quarterly financial report before submission to the board of directors |

  Scenario: Add todo with null title should return 400
    And the following todo to add:
      | title | description             | date_debut          | date_fin            |
      | null  | Rendre notes DIC projet | 2025-08-19T12:15:00 | 2025-08-19T12:30:00 |
    When call add todo
    Then the http status is 400
    And the returned error body looks like:
      | system_id                            | system_name | type       | status | message                  |
      | be7e84a8-9f56-405b-a15a-2646a8012c89 | MS-TODOS    | VALIDATION | 400    | title: title is required |

  Scenario Outline: AAdd todo with description not matching size constraintes should return 400
    And the following todo to add:
      | title        | description   | date_debut          | date_fin            |
      | Rendre notes | <description> | 2025-08-19T12:15:00 | 2025-08-19T12:30:00 |
    When call add todo
    Then the http status is 400
    And the returned error body looks like:
      | system_id                            | system_name | type       | status | message                                                 |
      | be7e84a8-9f56-405b-a15a-2646a8012c89 | MS-TODOS    | VALIDATION | 400    | description: description must not exceed 255 characters |
    Examples:
      | description                                                                                                                                                                                                                                                                                                               |
      | This task involves conducting a comprehensive code review of the entire microservices architecture to ensure consistency, scalability, and adherence to established design principles. All findings should be documented clearly, and any security vulnerabilities must be flagged for immediate resolution and follow-up |


  Scenario: AAdd todo with dateDebut null should return 400
    And the following todo to add:
      | title               | description             | date_debut | date_fin            |
      | Rendre notes projet | Rendre notes DIC projet | null       | 2025-08-19T12:30:00 |
    When call add todo
    Then the http status is 400
    And the returned error body looks like:
      | system_id                            | system_name | type       | status | message                          |
      | be7e84a8-9f56-405b-a15a-2646a8012c89 | MS-TODOS    | VALIDATION | 400    | dateDebut: dateDebut is required |

  Scenario: AAdd todo with dateFin null should return 400
    And the following todo to add:
      | title               | description             | date_debut          | date_fin |
      | Rendre notes projet | Rendre notes DIC projet | 2025-08-19T12:15:00 | null     |
    When call add todo
    Then the http status is 400
    And the returned error body looks like:
      | system_id                            | system_name | type       | status | message                      |
      | be7e84a8-9f56-405b-a15a-2646a8012c89 | MS-TODOS    | VALIDATION | 400    | dateFin: dateFin is required |


  Scenario: Add todo with dateDebut in the pass should return 400
    And the following todo to add:
      | title               | description             | date_debut          | date_fin            |
      | Rendre notes projet | Rendre notes DIC projet | 1900-08-19T12:15:00 | 2025-08-19T12:30:00 |
    When call add todo
    Then the http status is 400
    And the returned error body looks like:
      | system_id                            | system_name | type       | status | message                                    |
      | be7e84a8-9f56-405b-a15a-2646a8012c89 | MS-TODOS    | VALIDATION | 400    | dateDebut: dateDebut must be in the future |


  Scenario: Add todo with dateFin in the pass should return 400
    And the following todo to add:
      | title               | description             | date_debut          | date_fin            |
      | Rendre notes projet | Rendre notes DIC projet | 2025-08-19T12:15:00 | 1900-08-19T12:30:00 |
    When call add todo
    Then the http status is 400
    And the returned error body looks like:
      | system_id                            | system_name | type       | status | message                                |
      | be7e84a8-9f56-405b-a15a-2646a8012c89 | MS-TODOS    | VALIDATION | 400    | dateFin: dateFin must be in the future |


  Scenario: Add todo with dateDebut after dateFin should return 400
    And the following todo to add:
      | title               | description             | date_debut          | date_fin            |
      | Rendre notes projet | Rendre notes DIC projet | 2025-08-19T12:15:00 | 2025-08-19T12:00:00 |
    When call add todo
    Then the http status is 400
    And the returned error body looks like:
      | system_id                            | system_name | type       | status | message                                  |
      | be7e84a8-9f56-405b-a15a-2646a8012c89 | MS-TODOS    | VALIDATION | 400    | Field dateDebut should be before dateFin |

