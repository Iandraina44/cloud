import 'package:firebase_auth/firebase_auth.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
class AuthService {
  final FirebaseAuth _auth = FirebaseAuth.instance;
  final FirebaseFirestore _firestore = FirebaseFirestore.instance;

  Future<Map<String, dynamic>?> signInWithEmailAndPassword(
      String email, String password) async {
    try {
      QuerySnapshot querySnapshot = await _firestore
          .collection('utilisateurs')
          .where('email', isEqualTo: email)
          .limit(1)
          .get();

      if (querySnapshot.docs.isEmpty) {
        print("User not found in Firestore.");
        return null;
      }

      var userDoc = querySnapshot.docs.first;
      if (userDoc['mdp'] == password) {
        print("Login successful: ${userDoc.data()}");
        return {
          'id_utilisateur': userDoc['id_utilisateur'],
          'email': userDoc['email'],
          'url' : userDoc['url'],
        };
      } else {
        print("Incorrect password.");
        return null;
      }
    } catch (e) {
      print("Login error: ${e.toString()}");
      return null;
    }
  }
}
