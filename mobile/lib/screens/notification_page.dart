import 'package:flutter/material.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import '../services/notification_service.dart';

class NotificationPage extends StatefulWidget {
  final int userId;

  const NotificationPage({Key? key, required this.userId}) : super(key: key);

  @override
  _NotificationPageState createState() => _NotificationPageState();
}

class _NotificationPageState extends State<NotificationPage> {
  late Stream<List<Map<String, dynamic>>> _notificationsStream;

  @override
  void initState() {
    super.initState();
    // Listen to notifications in real-time through the service
    _notificationsStream = NotificationService().getNotificationsStream(widget.userId);
  }

  // Mark a notification as read
  Future<void> _markAsRead(String notificationId) async {
    if (notificationId.isNotEmpty) {
      await NotificationService().markAsRead(notificationId);
    }
  }

  // Mark all notifications as read
  Future<void> _markAllAsRead() async {
    await NotificationService().markAllAsRead(widget.userId);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Notifications")),
      body: StreamBuilder<List<Map<String, dynamic>>>(
        stream: _notificationsStream,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(child: CircularProgressIndicator());
          }

          if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          }

          var notifications = snapshot.data ?? [];

          return notifications.isEmpty
              ? Center(child: Text('No unread notifications'))
              : ListView.builder(
            itemCount: notifications.length,
            itemBuilder: (context, index) {
              var notification = notifications[index];
              return ListTile(
                title: Text(notification['message'] ?? 'No message'),
                trailing: IconButton(
                  icon: Icon(Icons.check),
                  onPressed: () => _markAsRead(notification['id'] ?? ''),
                ),
              );
            },
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _markAllAsRead,
        child: Icon(Icons.done_all),
        tooltip: 'Mark all as read',
      ),
    );
  }
}
