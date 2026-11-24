# Validation manuelle de l'atelier DTO & Validators

## Vérifier la liste des agences

Lancer l'API, puis appeler `GET http://localhost:8080/api/agences`. La réponse est une liste d'objets `AgenceDto` (identifiant, nom, ville, adresse, téléphone et effectif); elle n'embarque pas les collections JPA de véhicules et d'employés.

## Vérifier les validations véhicule

Envoyer vers `POST http://localhost:8080/api/vehicules` un JSON invalide, par exemple `{ "immatriculation": "", "marque": "", "modele": "", "categorie": null, "tarifJournalier": 0, "statut": null }`. Le serveur répond `400 Bad Request` avec un objet `fieldErrors` contenant les messages pour chaque champ invalide.

Répéter le même appel avec `PUT http://localhost:8080/api/vehicules/{id}` pour vérifier la validation lors d'une modification. Un JSON valide utilise les champs `immatriculation`, `marque`, `modele`, `categorie`, `tarifJournalier`, `statut` et, facultativement, `agenceId`.

## Installation / analyse

Exécuter `mvn clean install`, puis l'analyse SonarQube selon la configuration du projet. Le dépôt Maven Central doit être accessible pour télécharger MapStruct et son processeur d'annotations.
