import 'package:cloud_firestore/cloud_firestore.dart';

class FondService {
  final FirebaseFirestore _firestore = FirebaseFirestore.instance;

  // Ajouter une nouvelle opération (retrait ou dépôt)
  Future<void> ajouterFond({
    required double valeurFond,
    required String operation,
    required int idUtilisateur,
  }) async {
    try {
      await _firestore.collection('fond').add({
        'valeur_fond': valeurFond,
        'operation': operation,
        'id_utilisateur': idUtilisateur,
      });
      print("Opération ajoutée avec succès");
    } catch (e) {
      print("Erreur lors de l'ajout de l'opération : $e");
    }
  }
}
