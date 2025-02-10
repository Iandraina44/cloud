import 'package:cloud_firestore/cloud_firestore.dart';
import '../services/historique_transaction.dart';

class VenteService {
  final FirebaseFirestore _firestore = FirebaseFirestore.instance;

  Stream<List<HistoriqueTransaction>> getVenteStream(int userId) {
    return _firestore
        .collection('vente')
        .where('id_utilisateur', isEqualTo: userId)
        .snapshots() // ❌ Removed orderBy
        .map((snapshot) => snapshot.docs
        .map((doc) => HistoriqueTransaction.fromDocumentVente(doc))
        .toList());
  }
}
