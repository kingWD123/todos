#see https://cucumber.io/docs/gherkin/reference/
Feature:  BDD Scenarios of Todo API - Find All Todos

  Background:
    Given table todo contains data:
      | id                                   | title        | description        | completed | date_debut          | date_fin            |
      | 17a281a6-0882-4460-9d95-9c28f5852db1 | Rendre notes | Rendre notes DIC 1 | false     | 2025-08-19T12:00:00 | 2025-08-19T12:15:00 |
      | 18a281a6-0882-4460-9d95-9c28f5852db1 | Presentation | Presentation DIC 1 | false     | 2025-08-12T15:00:00 | 2030-09-02T19:00:00 |

  Scenario: Find all should return correct page
    When call find all  with page=0, size=10 and sort="sort=title,asc&sort=description,asc"
    Then the http status is 200
    And the returned page has following content:
      | title        | description        | completed | date_debut          | date_fin            |
      | Rendre notes | Rendre notes DIC 1 | false     | 2025-08-19T12:00:00 | 2025-08-19T12:15:00 |
      | Presentation | Presentation DIC 1 | false     | 2025-08-12T15:00:00 | 2030-09-02T19:00:00 |

  Scenario: Find all should return empty page
    When call find all  with page=1, size=10 and sort="sort=title,asc&sort=description,asc"
    Then the http status is 200
    And the returned page has no content

  Scenario: Find all with negative page should return error
    When call find all  with page=-1, size=10 and sort="sort=title,asc&sort=description,asc"
    Then the http status is 400
    And the returned error body looks like:
      | system_id                            | system_name | type       | status | message                               |
      | be7e84a8-9f56-405b-a15a-2646a8012c89 | MS-TODOS    | VALIDATION | 400    | Page index must not be less than zero |

  Scenario: Find all with size less than 1 should return error
    When call find all  with page=0, size=0 and sort="sort=title,asc&sort=description,asc"
    Then the http status is 400
    And the returned error body looks like:
      | system_id                            | system_name | type       | status | message                             |
      | be7e84a8-9f56-405b-a15a-2646a8012c89 | MS-TODOS    | VALIDATION | 400    | Page size must not be less than one |

  Scenario: Find all with size too large should return error
    When call find all  with page=0, size=100 and sort="sort=title,asc&sort=description,asc"
    Then the http status is 400
    And the returned error body looks like:
      | system_id                            | system_name | type       | status | message                         |
      | be7e84a8-9f56-405b-a15a-2646a8012c89 | MS-TODOS    | VALIDATION | 400    | Page size must not be too large |

