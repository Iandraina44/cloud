use cloud;

CREATE TABLE utilisateur(
   id_utilisateur INT AUTO_INCREMENT,
   email VARCHAR(256)  NOT NULL,
   mdp VARCHAR(256)  NOT NULL,
   url VARCHAR(250) ,
   PRIMARY KEY(id_utilisateur)
);

CREATE TABLE inscription(
   id_inscription INT AUTO_INCREMENT,
   email VARCHAR(256)  NOT NULL,
   mdp VARCHAR(256)  NOT NULL,
   date_entree DATETIME,
   random_token VARCHAR(16)  NOT NULL,
   date_validation DATETIME,
   PRIMARY KEY(id_inscription)
);

CREATE TABLE authentification(
   id_authentification INT AUTO_INCREMENT,
   pin VARCHAR(4)  NOT NULL,
   date_debut DATETIME NOT NULL,
   date_fin DATETIME NOT NULL,
   id_utilisateur INT NOT NULL,
   PRIMARY KEY(id_authentification),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);

CREATE TABLE token_historique(
   id_token_historique INT AUTO_INCREMENT,
   token_utilisateur VARCHAR(256)  NOT NULL,
   date_debut DATETIME NOT NULL,
   date_fin DATETIME NOT NULL,
   id_utilisateur INT NOT NULL,
   PRIMARY KEY(id_token_historique),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);

CREATE TABLE historique_connexion(
   id_historique_connexion INT AUTO_INCREMENT,
   date_tentative DATETIME NOT NULL,
   id_utilisateur INT NOT NULL,
   PRIMARY KEY(id_historique_connexion),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);

CREATE TABLE connexion_echoue(
   id_connexion_echoue INT AUTO_INCREMENT,
   nombre INT NOT NULL,
   id_utilisateur INT NOT NULL,
   PRIMARY KEY(id_connexion_echoue),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);

CREATE TABLE cryptomonaie(
   id_cryptomonaie INT AUTO_INCREMENT,
   nom_cryptomonaie VARCHAR(256)  NOT NULL,
   PRIMARY KEY(id_cryptomonaie)
);

CREATE TABLE cours_crypto(
   id_cour INT AUTO_INCREMENT,
   date_cour DATETIME NOT NULL,
   valeur_cour DECIMAL(15,2)   NOT NULL,
   id_cryptomonaie INT NOT NULL,
   PRIMARY KEY(id_cour),
   FOREIGN KEY(id_cryptomonaie) REFERENCES cryptomonaie(id_cryptomonaie)
);

CREATE TABLE portefeuille_crypto(
   id_portefeuille INT AUTO_INCREMENT,
   date_portefeuille DATETIME NOT NULL,
   valeur_portefeuille DECIMAL(15,2)   NOT NULL,
   id_utilisateur INT NOT NULL,
   id_cryptomonaie INT NOT NULL,
   PRIMARY KEY(id_portefeuille),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur),
   FOREIGN KEY(id_cryptomonaie) REFERENCES cryptomonaie(id_cryptomonaie)
);

CREATE TABLE fond(
   id_fond INT AUTO_INCREMENT,
   valeur_fond DECIMAL(15,2)   NOT NULL,
   date_fond DATETIME NOT NULL,
   etat INT,
   id_utilisateur INT NOT NULL,
   PRIMARY KEY(id_fond),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);

CREATE TABLE achat(
   id_achat INT AUTO_INCREMENT,
   date_achat DATETIME NOT NULL,
   quantite INT NOT NULL,
   prix_unitaire DECIMAL(15,2)  ,
   commission_appliquee DECIMAL(15,2)  ,
   id_utilisateur INT,
   id_cryptomonaie INT,
   PRIMARY KEY(id_achat),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur),
   FOREIGN KEY(id_cryptomonaie) REFERENCES cryptomonaie(id_cryptomonaie)
);

CREATE TABLE vente(
   id_vente INT AUTO_INCREMENT,
   date_vente DATETIME NOT NULL,
   quantite INT NOT NULL,
   prix_unitaire DECIMAL(15,2)  ,
   commission_appliquee DECIMAL(15,2)  ,
   id_utilisateur INT,
   id_cryptomonaie INT,
   PRIMARY KEY(id_vente),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur),
   FOREIGN KEY(id_cryptomonaie) REFERENCES cryptomonaie(id_cryptomonaie)
);

CREATE TABLE commission(
   id_commission INT AUTO_INCREMENT,
   valeur DECIMAL(15,2)  ,
   date_modification DATETIME,
   type_commission VARCHAR(50) ,
   PRIMARY KEY(id_commission)
);

CREATE TABLE notification(
   id_notification INT AUTO_INCREMENT,
   message VARCHAR(250)  NOT NULL,
   etat INT NOT NULL,
   date_notification DATETIME NOT NULL,
   id_utilisateur INT NOT NULL,
   PRIMARY KEY(id_notification),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);

CREATE TABLE favoris(
   id_favoris INT AUTO_INCREMENT,
   id_cryptomonaie INT NOT NULL,
   id_utilisateur INT NOT NULL,
   PRIMARY KEY(id_favoris),
   FOREIGN KEY(id_cryptomonaie) REFERENCES cryptomonaie(id_cryptomonaie),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);


CREATE TABLE admin_t(
   id_admin INT AUTO_INCREMENT,
   username VARCHAR(50)  NOT NULL,
   mdp VARCHAR(256)  NOT NULL,
   PRIMARY KEY(id_admin)
);

DELIMITER //

CREATE PROCEDURE get_portefeuille(IN date_max DATETIME)
BEGIN
    SELECT 
        u.id_utilisateur,
        COALESCE(total_achat.total_achat, 0) AS total_achat,
        COALESCE(total_vente.total_vente, 0) AS total_vente,
        COALESCE(total_fond.total_fond, 0) AS total_fond
    FROM utilisateur u
    LEFT JOIN (
        SELECT 
            a.id_utilisateur,
            SUM(a.quantite * a.prix_unitaire) AS total_achat
        FROM achat a
        WHERE a.date_achat <= date_max
        GROUP BY a.id_utilisateur
    ) total_achat ON u.id_utilisateur = total_achat.id_utilisateur
    LEFT JOIN (
        SELECT 
            v.id_utilisateur,
            SUM(v.quantite * v.prix_unitaire) AS total_vente
        FROM vente v
        WHERE v.date_vente <= date_max
        GROUP BY v.id_utilisateur
    ) total_vente ON u.id_utilisateur = total_vente.id_utilisateur
    LEFT JOIN (
        SELECT 
            p.id_utilisateur,
            SUM(p.valeur_portefeuille * c.valeur_cour) AS total_fond
        FROM portefeuille_crypto p
        LEFT JOIN (
            SELECT 
                id_cryptomonaie, 
                MAX(date_cour) AS max_date
            FROM cours_crypto
            WHERE date_cour <= date_max
            GROUP BY id_cryptomonaie
        ) latest ON p.id_cryptomonaie = latest.id_cryptomonaie
        LEFT JOIN cours_crypto c ON c.id_cryptomonaie = latest.id_cryptomonaie AND c.date_cour = latest.max_date
        WHERE p.date_portefeuille <= date_max
        GROUP BY p.id_utilisateur
    ) total_fond ON u.id_utilisateur = total_fond.id_utilisateur;
END //

DELIMITER ;



INSERT INTO admin_t (username,mdp) VALUES
('admin',sha1('admin'));


-- Insérer des cryptomonnaies
INSERT INTO cryptomonaie ( nom_cryptomonaie) VALUES 
( 'Bitcoin'),
( 'Ethereum'),
( 'Ripple');

-- Insérer des cours pour chaque cryptomonnaie à des dates différentes
INSERT INTO cours_crypto ( date_cour, valeur_cour, id_cryptomonaie) VALUES 
( '2024-12-18 10:00:00', 9500.50, 1), -- Bitcoin
( '2024-12-19 10:00:00', 7600.75, 1), -- Bitcoin
( '2024-12-18 12:00:00', 3000.00, 2),  -- Ethereum
( '2024-12-19 11:00:00', 3100.45, 2),  -- Ethereum
( '2024-12-18 14:00:00', 1000.75, 3),    -- Ripple
( '2024-12-19 13:00:00', 5980.78, 3);    -- Ripple

INSERT INTO commission( valeur, date_modification, type_commission) VALUES 
( 5, NOW(), 'achat'),
( 5, NOW(), 'vente');
