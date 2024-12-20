CREATE TABLE utilisateur(
   id_utilisateur INT,
   email VARCHAR(256) NOT NULL,
   mdp VARCHAR(256) NOT NULL,
   PRIMARY KEY(id_utilisateur)
);

CREATE TABLE inscription(
   id_inscription INT,
   email VARCHAR(256) NOT NULL,
   mdp VARCHAR(256) NOT NULL,
   date_entree DATETIME,
   random_token VARCHAR(16) NOT NULL,
   date_validation DATETIME,
   PRIMARY KEY(id_inscription)
);

CREATE TABLE authentification(
   id_authentification INT,
   pin VARCHAR(4) NOT NULL,
   date_debut DATETIME NOT NULL,
   date_fin DATETIME NOT NULL,
   id_utilisateur INT NOT NULL,
   PRIMARY KEY(id_authentification),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);

CREATE TABLE token_historique(
   id_token_historique INT,
   token_utilisateur VARCHAR(256) NOT NULL,
   date_debut DATETIME NOT NULL,
   date_fin DATETIME NOT NULL,
   id_utilisateur INT NOT NULL,
   PRIMARY KEY(id_token_historique),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);

CREATE TABLE historique_connexion(
   id_historique_connexion INT,
   date_tentative DATETIME NOT NULL,
   id_utilisateur INT NOT NULL,
   PRIMARY KEY(id_historique_connexion),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);

CREATE TABLE connexion_echoue(
   id_connexion_echoue INT,
   nombre INT NOT NULL,
   id_utilisateur INT NOT NULL,
   PRIMARY KEY(id_connexion_echoue),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);

CREATE TABLE cryptomonaie(
   id_cryptomonaie INT,
   nom_cryptomonaie VARCHAR(256) NOT NULL,
   PRIMARY KEY(id_cryptomonaie)
);

CREATE TABLE cours_crypto(
   id_cour INT,
   date_cour DATETIME NOT NULL,
   valeur_cour DECIMAL(15,2) NOT NULL,
   id_cryptomonaie INT NOT NULL,
   PRIMARY KEY(id_cour),
   FOREIGN KEY(id_cryptomonaie) REFERENCES cryptomonaie(id_cryptomonaie)
);

CREATE TABLE portefeuille_crypto(
   id_portefeuille INT,
   date_portefeuille DATETIME NOT NULL,
   valeur_portefeuille DECIMAL(15,2) NOT NULL,
   id_utilisateur INT NOT NULL,
   id_cryptomonaie INT NOT NULL,
   PRIMARY KEY(id_portefeuille),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur),
   FOREIGN KEY(id_cryptomonaie) REFERENCES cryptomonaie(id_cryptomonaie)
);

CREATE TABLE fond(
   id_fond INT,
   valeur_fond DECIMAL(15,2) NOT NULL,
   date_fond DATETIME NOT NULL,
   id_utilisateur INT NOT NULL,
   PRIMARY KEY(id_fond),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);
