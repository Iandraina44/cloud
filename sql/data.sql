-- Insérer des cryptomonnaies
INSERT INTO cryptomonaie (id_cryptomonaie, nom_cryptomonaie) VALUES 
(1, 'Bitcoin'),
(2, 'Ethereum'),
(3, 'Ripple');

-- Insérer des cours pour chaque cryptomonnaie à des dates différentes
INSERT INTO cours_crypto (id_cour, date_cour, valeur_cour, id_cryptomonaie) VALUES 
(1, '2024-12-18 10:00:00', 45000.50, 1), -- Bitcoin
(2, '2024-12-19 10:00:00', 46000.75, 1), -- Bitcoin
(3, '2024-12-18 12:00:00', 3000.00, 2),  -- Ethereum
(4, '2024-12-19 11:00:00', 3100.45, 2),  -- Ethereum
(5, '2024-12-18 14:00:00', 0.75, 3),    -- Ripple
(6, '2024-12-19 13:00:00', 0.78, 3);    -- Ripple
