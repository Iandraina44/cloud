import 'package:cloud_firestore/cloud_firestore.dart';
import 'dart:async';

class NotificationService {
  final FirebaseFirestore _firestore = FirebaseFirestore.instance;

  // StreamController pour diffuser les notifications mises à jour
  final StreamController<List<Map<String, dynamic>>> _notificationsController =
  StreamController<List<Map<String, dynamic>>>();

  // Méthode pour obtenir le stream des notifications
  Stream<List<Map<String, dynamic>>> getNotificationsStream(int userId) {
    // Listen for unread notifications (etat == 0)
    return _firestore
        .collection('notifications')
        .where('id_utilisateur', isEqualTo: userId) // Pass userId here
        .where('etat', isEqualTo: 0) // Only show unread notifications
        .snapshots()
        .map((snapshot) {
      return snapshot.docs.map((doc) {
        var data = doc.data() as Map<String, dynamic>;
        return {
          'id': doc.id,
          'message': data['message'] ?? 'No message',
          'etat': data['etat'] ?? 0,
        };
      }).toList();
    });
  }


  // Ajouter une nouvelle notification
  Future<void> addNotification(int userId, String message) async {
    try {
      await _firestore.collection('notifications').add({
        'id_utilisateur': userId,
        'message': message,
        'etat': 0, // Etat 0 pour non lu
        'timestamp': FieldValue.serverTimestamp(),
      });
      print('Notification added successfully');
    } catch (e) {
      print('Error adding notification: $e');
    }
  }

  // Marquer une notification comme lue
  Future<void> markAsRead(String notificationId) async {
    try {
      await _firestore
          .collection('notifications')
          .doc(notificationId)
          .update({'etat': 1}); // Etat 1 pour lu
    } catch (e) {
      print('Error marking notification as read: $e');
    }
  }

  // Marquer toutes les notifications comme lues
  Future<void> markAllAsRead(int userId) async {
    var batch = FirebaseFirestore.instance.batch();
    var snapshot = await _firestore
        .collection('notifications')
        .where('id_utilisateur', isEqualTo: userId)
        .where('etat', isEqualTo: 0) // Non lu
        .get();

    for (var doc in snapshot.docs) {
      batch.update(doc.reference, {'etat': 1});
    }
    await batch.commit();
    // Diffuser à nouveau les notifications après la mise à jour
    var updatedNotifications = await _getUpdatedNotifications(userId);
    _notificationsController.add(updatedNotifications);
  }

  // Récupérer les notifications mises à jour
  Future<List<Map<String, dynamic>>> _getUpdatedNotifications(int userId) async {
    var snapshot = await _firestore
        .collection('notifications')
        .where('id_utilisateur', isEqualTo: userId)
        .get();

    return snapshot.docs.map((doc) {
      var data = doc.data() as Map<String, dynamic>;
      return {
        'id': doc.id,
        'message': data['message'] ?? 'No message',
        'etat': data['etat'] ?? 0,
      };
    }).toList();
  }

  // Compter les notifications non lues
  Stream<int> getUnreadNotificationsCountStream(int userId) {
    return _firestore
        .collection('notifications')
        .where('id_utilisateur', isEqualTo: userId)
        .where('etat', isEqualTo: 0) // Only unread notifications
        .snapshots()
        .map((snapshot) => snapshot.docs.length);
  }
  // Fermer le StreamController lors de la suppression du service
  void dispose() {
    _notificationsController.close();
  }
}
