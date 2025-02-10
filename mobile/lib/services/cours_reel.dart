import 'package:cloud_firestore/cloud_firestore.dart';

class CoursReel {
  final FirebaseFirestore _firestore = FirebaseFirestore.instance;

  /// Récupère la liste des cryptos et leurs valeurs depuis Firestore en temps réel
  Stream<List<Map<String, dynamic>>> getCryptoDataStream() {
    try {
      // Listen to real-time changes in the 'coursreel' collection
      return _firestore.collection('cours_crypto').snapshots().map((snapshot) {
        return snapshot.docs.map((doc) {
          return {
            "id_cryptomonaie" :doc['id_cryptomonaie'],
            "nom": doc['nom_cryptomonaie'],
            "valeur": (doc['valeur_cour'] as num).toDouble(),
          };
        }).toList();
      });
    } catch (e) {
      print("Erreur lors de la récupération des cryptos en temps réel: $e");
      return Stream.value([]); // Return an empty list in case of an error
    }
  }
}
