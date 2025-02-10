import 'package:cloud_firestore/cloud_firestore.dart';

/// Modèle représentant une entrée du portefeuille
class Portefeuille {
  final String idCrypto;
  final int idUtilisateur; // On attend un int ici
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
      // Conversion de id_crypto en chaîne, même si c'est un int dans Firestore
      idCrypto: (data['id_crypto'] ?? '').toString(),
      idUtilisateur: data['id_utilisateur'] ?? 0,
      nomCrypto: data['nom_crypto'] ?? '',
      quantite: (data['quantite'] as num?)?.toDouble() ?? 0.0,
    );
  }
}

/// Service gérant la récupération des données depuis Firestore
class PortefeuilleService {
  final FirebaseFirestore _firestore = FirebaseFirestore.instance;

  /// Retourne un stream du portefeuille de l'utilisateur dont l'ID est [userId]
  Stream<List<Portefeuille>> getPortefeuilleByUserStream(int userId) {
    return _firestore
        .collection('portefeuille')
        .where('id_utilisateur', isEqualTo: userId)
        .snapshots()
        .map((querySnapshot) => querySnapshot.docs
        .map((doc) => Portefeuille.fromDocument(doc))
        .toList());
  }
}
