CREATE DATABASE cloud;
USE cloud;

CREATE TABLE utilisateur (
   id_utilisateur INT AUTO_INCREMENT,
   email VARCHAR(256) NOT NULL,
   mdp VARCHAR(256) NOT NULL,
   PRIMARY KEY(id_utilisateur)
);

CREATE TABLE inscription (
   id_inscription INT AUTO_INCREMENT,
   email VARCHAR(256) NOT NULL,
   mdp VARCHAR(256) NOT NULL,
   date_entree DATETIME,
   random_token VARCHAR(16) NOT NULL,
   date_validation DATETIME,
   PRIMARY KEY(id_inscription)
);

CREATE TABLE authentification (
   id_authentification INT AUTO_INCREMENT,
   pin VARCHAR(4) NOT NULL,
   date_debut DATETIME NOT NULL,
   date_fin DATETIME NOT NULL,
   id_utilisateur INT NOT NULL,
   PRIMARY KEY(id_authentification),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);

CREATE TABLE role (
   id_role INT AUTO_INCREMENT,
   description VARCHAR(256) NOT NULL,
   PRIMARY KEY(id_role)
);

CREATE TABLE token_historique (
   id_token_historique INT AUTO_INCREMENT,
   token_utilisateur VARCHAR(256) NOT NULL,
   date_debut DATETIME NOT NULL,
   date_fin DATETIME NOT NULL,
   id_utilisateur INT NOT NULL,
   PRIMARY KEY(id_token_historique),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);

CREATE TABLE historique_connexion (
   id_historique_connexion INT AUTO_INCREMENT,
   date_tentative DATETIME NOT NULL,
   id_utilisateur INT NOT NULL,
   PRIMARY KEY(id_historique_connexion),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);

CREATE TABLE connexion_echoue (
   id_connexion_echoue INT AUTO_INCREMENT,
   nombre INT NOT NULL,
   id_utilisateur INT NOT NULL,
   PRIMARY KEY(id_connexion_echoue),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);
