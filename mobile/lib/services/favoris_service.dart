import 'package:cloud_firestore/cloud_firestore.dart';

class FavorisService {
  final FirebaseFirestore _firestore = FirebaseFirestore.instance;

  // Add a new favorite
  Future<void> ajouterFavori(int idCryptomonnaie, int idUtilisateur) async {
    try {
      await _firestore.collection('favoris').add({
        'id_cryptomonnaie': idCryptomonnaie,
        'id_utilisateur': idUtilisateur,
        'synchro': 0,  // Example to mark the favorite as synchronized
      });
      print("Favori ajouté avec succès");
    } catch (e) {
      print("Erreur lors de l'ajout du favori : $e");
    }
  }

  // Remove a favorite
  Future<void> supprimerFavori(int idCryptomonnaie, int idUtilisateur) async {
    try {
      QuerySnapshot snapshot = await _firestore
          .collection('favoris')
          .where('id_cryptomonnaie', isEqualTo: idCryptomonnaie)
          .where('id_utilisateur', isEqualTo: idUtilisateur)
          .get();

      for (var doc in snapshot.docs) {
        await _firestore.collection('favoris').doc(doc.id).delete();
        print("Favori supprimé avec succès");
      }
    } catch (e) {
      print("Erreur lors de la suppression du favori : $e");
    }
  }

  // Check if a cryptocurrency is already a favorite
  Future<bool> checkIfFavori(int idCryptomonnaie, int idUtilisateur) async {
    try {
      QuerySnapshot snapshot = await _firestore
          .collection('favoris')
          .where('id_cryptomonnaie', isEqualTo: idCryptomonnaie)
          .where('id_utilisateur', isEqualTo: idUtilisateur)
          .get();

      return snapshot.docs.isNotEmpty;
    } catch (e) {
      print("Erreur lors de la vérification du favori : $e");
      return false;
    }
  }
}
