import 'package:cloud_firestore/cloud_firestore.dart';

class HistoriqueTransaction {
  final String idCryptomonaie;
  final String nomCryptomonaie;
  final double quantite;
  final DateTime dateTransaction;
  final int idUtilisateur;

  HistoriqueTransaction({
    required this.idCryptomonaie,
    required this.nomCryptomonaie,
    required this.quantite,
    required this.dateTransaction,
    required this.idUtilisateur,
  });

  /// Crée une instance à partir d'un document Firestore (ACHAT)
  factory HistoriqueTransaction.fromDocumentAchat(DocumentSnapshot doc) {
    final data = doc.data() as Map<String, dynamic>;
    return HistoriqueTransaction(
      idCryptomonaie: data['id_cryptomonaie']?.toString() ?? '', // ✅ Convert to String
      nomCryptomonaie: data['nom_cryptomonaie']?.toString() ?? '', // ✅ Convert to String
      quantite: (data['quantite'] as num?)?.toDouble() ?? 0.0, // ✅ Ensure it's a double
      dateTransaction: (data['date_achat'] as Timestamp).toDate(),
      idUtilisateur: data['id_utilisateur'] is int
          ? data['id_utilisateur']
          : int.tryParse(data['id_utilisateur'].toString()) ?? 0, // ✅ Convert to int safely
    );
  }

  /// Crée une instance à partir d'un document Firestore (VENTE)
  factory HistoriqueTransaction.fromDocumentVente(DocumentSnapshot doc) {
    final data = doc.data() as Map<String, dynamic>;
    return HistoriqueTransaction(
      idCryptomonaie: data['id_cryptomonaie']?.toString() ?? '',
      nomCryptomonaie: data['nom_cryptomonaie']?.toString() ?? '',
      quantite: (data['quantite'] as num?)?.toDouble() ?? 0.0,
      dateTransaction: (data['date_vente'] as Timestamp).toDate(),
      idUtilisateur: data['id_utilisateur'] is int
          ? data['id_utilisateur']
          : int.tryParse(data['id_utilisateur'].toString()) ?? 0,
    );
  }
}
