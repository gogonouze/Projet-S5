# Projet-S5
[![forthebadge](https://forthebadge.com/images/badges/gluten-free.svg)](http://forthebadge.com)  [![forthebadge](https://forthebadge.com/images/badges/powered-by-responsibility.svg)](http://forthebadge.com)
## [FR]
Projet L3S5 Informatique

### Interface de communication par tickets entre clients et opérateurs :
* Création de ticket
* Redirection du ticket par le serveur vers un opérateur compétant
* Communication client-opérateur via serveur
* ~~Clôture du ticket~~
* Conservation de l'historique de la communication utilisateur/serveur
* Interface pour les utilisateurs
* Création de compte

### Pré-requis

* [Java](https://www.java.com/fr/) - à jours
* [WampServer](http://www.wampserver.com) - à jours
* [mysql-connector-java](https://mvnrepository.com/artifact/mysql/mysql-connector-java/8.0.13) - version 8.0.13

### Installation

* Téléchargez les exécutables java :
    - Server.jar
    - Client.jar
    - Operator.jar

* Pour créer la base de données :
    - Allumer **[wamp](http://www.wampserver.com)**
    - Ouvrir **phpMyAdmin**
    - Utilisateur : **root** | Mot de passe : *(rien)*
    - Créer une nouvelle base de données qui s'appelle **bdd_projet_s5**
    - Dans cette base de données importer le fichier **bdd_projet_s5.sql** qui se trouve à la **racine du projet**
    - Dans **eclipse** ajouter le plug-in **mysql-connector-java**

### Démarrage

 * Lancer un **serveur** avec **Server.jar**
 * Lancer un **client** ou un **operateur** avec **Client.jar** ou **Operator.jar**

### Fabriqué avec

* [Amour](https://fr.wikipedia.org/wiki/Amour)
* [Eclipse](https://www.eclipse.org) - IDE java


### Auteurs
* **EB-LEVADOUX Ugo** - *Initial work* - [gogonouze](https://github.com/gogonouze)
* **MEUNIER Romain** **[MVP⭐]** - *Initial work* - [UnkneesDwark](https://github.com/UnkneesDwark)
* **RIGAL Pierre** - *Initial work* - [Nibellung](https://github.com/Nibellung)
