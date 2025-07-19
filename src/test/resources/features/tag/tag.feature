Feature:  BDD Scenarios of Tag API

  # Création d'un tag
  Scenario: Création d'un nouveau tag
    Given l'utilisateur est authentifié
    When il crée un tag nommé "Important"
    Then le tag "Important" doit exister dans la liste des tags

  # Lecture des tags
  Scenario: Consultation de la liste des tags
    Given plusieurs tags existent
      | Important |
      | Travail   |
      | Perso     |
    When l'utilisateur consulte la liste des tags
    Then il voit tous les tags existants
      | Important |
      | Travail   |
      | Perso     |

  # Modification d'un tag
  Scenario: Modification d'un tag existant
    Given un tag nommé "Travail" existe
    When l'utilisateur renomme le tag "Travail" en "Professionnel"
    Then le tag "Professionnel" doit exister et "Travail" ne doit plus exister

  # Suppression d'un tag
  Scenario: Suppression d'un tag
    Given un tag nommé "Temporaire" existe
    When l'utilisateur supprime le tag "Temporaire"
    Then le tag "Temporaire" ne doit plus exister dans la liste des tags
