import 'package:cloud_firestore/cloud_firestore.dart';
import '../services/historique_transaction.dart';

class AchatService {
  final FirebaseFirestore _firestore = FirebaseFirestore.instance;

  Stream<List<HistoriqueTransaction>> getAchatStream(int userId) {
    return _firestore
        .collection('achat')
        .where('id_utilisateur', isEqualTo: userId)
        .snapshots() // ❌ Removed orderBy
        .map((snapshot) => snapshot.docs
        .map((doc) => HistoriqueTransaction.fromDocumentAchat(doc))
        .toList());
  }
}
