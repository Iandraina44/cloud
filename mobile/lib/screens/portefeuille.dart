// models/portefeuille.dart

import 'package:cloud_firestore/cloud_firestore.dart';

class Portefeuille {
  final String idCrypto;
  final String idUtilisateur;
  final String nomCrypto;
  final double quantite;

  Portefeuille({
    required this.idCrypto,
    required this.idUtilisateur,
    required this.nomCrypto,
    required this.quantite,
  });

  /// Crée une instance de Portefeuille à partir d'un document Firestore
  factory Portefeuille.fromDocument(DocumentSnapshot doc) {
    final data = doc.data() as Map<String, dynamic>;
    return Portefeuille(
      idCrypto: data['id_crypto'] ?? '',
      idUtilisateur: data['id_utilisateur'] ?? '',
      nomCrypto: data['nom_crypto'] ?? '',
      quantite: (data['quantite'] as num?)?.toDouble() ?? 0.0,
    );
  }
}
