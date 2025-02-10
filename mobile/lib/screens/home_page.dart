import 'package:flutter/material.dart';
import 'package:fl_chart/fl_chart.dart';
import 'package:cloud_firestore/cloud_firestore.dart'; // Firebase import
import '../services/cours_reel.dart';
import '../services/favoris_service.dart';
import '../services/notification_service.dart'; // Import the notification service
import 'fond_page.dart';
import 'portefeuille_page.dart';
import 'historique_page.dart';
import 'notification_page.dart'; // Import notification page
import 'profil_page.dart'; // Import notification page

class HomePage extends StatefulWidget {
  final String title;
  final String email;
  final int idUtilisateur;
  final String url;

  const HomePage({
    Key? key,
    required this.title,
    required this.email,
    required this.idUtilisateur,
    required this.url,
  });

  @override
  State<HomePage> createState() => _HomePageState();
}
// HomePage.dart

class _HomePageState extends State<HomePage> {
  final CoursReel _coursReel = CoursReel();
  final FavorisService _favorisService = FavorisService();
  final NotificationService _notificationService = NotificationService();

  Map<int, bool> favorisStatus = {};

  @override
  void initState() {
    super.initState();
    _loadFavorisStatus();
  }

  Future<void> _loadFavorisStatus() async {
    List<Map<String, dynamic>> cryptoData = await _coursReel
        .getCryptoDataStream()
        .first;
    for (var crypto in cryptoData) {
      bool isFavorite = await _favorisService.checkIfFavori(
          crypto['id_cryptomonaie'], widget.idUtilisateur);
      setState(() {
        favorisStatus[crypto['id_cryptomonaie']] = isFavorite;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
        actions: [
          // Add notification icon with the unread notifications count
          StreamBuilder<int>(
            stream: _notificationService.getUnreadNotificationsCountStream(
                widget.idUtilisateur),
            builder: (context, snapshot) {
              if (snapshot.connectionState == ConnectionState.waiting) {
                return const Center(child: CircularProgressIndicator());
              }
              if (snapshot.hasError) {
                return Icon(Icons.notifications);
              }
              int unreadNotificationsCount = snapshot.data ?? 0;
              return IconButton(
                icon: Stack(
                  children: [
                    Icon(Icons.notifications),
                    if (unreadNotificationsCount > 0)
                      Positioned(
                        right: 0,
                        top: 0,
                        child: CircleAvatar(
                          radius: 10,
                          backgroundColor: Colors.red,
                          child: Text(
                            unreadNotificationsCount.toString(),
                            style: TextStyle(fontSize: 12, color: Colors.white),
                          ),
                        ),
                      ),
                  ],
                ),
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) =>
                          NotificationPage(userId: widget.idUtilisateur),
                    ),
                  );
                },
              );
            },
          ),
        ],
      ),
      drawer: Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: <Widget>[
            DrawerHeader(
              decoration: const BoxDecoration(
                color: Colors.blue,
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  GestureDetector(
                    onTap: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => ProfilePage(
                            userId: widget.idUtilisateur,
                            imageUrl: widget.url,
                          ),
                        ),
                      );
                    },
                    child: CircleAvatar(
                      radius: 40,
                      backgroundImage: NetworkImage(widget.url),
                    ),
                  ),
                  const SizedBox(height: 10),
                  Text(
                    ' ${widget.email}',
                    style: const TextStyle(
                      color: Colors.white,
                      fontSize: 14,
                    ),
                  ),
                ],
              ),
            ),
            ListTile(
              title: const Text('DEPOT ET RETRAIT'),
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => FondPage(idUtilisateur: widget.idUtilisateur)),
                );
              },
            ),
            ListTile(
              title: const Text('PORTE FEUILLE'),
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => PortefeuillePage(userId: widget.idUtilisateur)),
                );
              },
            ),
            ListTile(
              title: const Text('HISTORIQUE ACHAT VENTE'),
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => HistoriquePage(userId: widget.idUtilisateur)),
                );
              },
            ),
          ],
        ),
      ),

      body: StreamBuilder<List<Map<String, dynamic>>>(
        stream: _coursReel.getCryptoDataStream(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }
          if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          }
          if (!snapshot.hasData || snapshot.data!.isEmpty) {
            return const Center(child: Text('No data available'));
          }

          List<Map<String, dynamic>> cryptoData = snapshot.data!;
          double maxY = getMaxY(cryptoData);

          return SingleChildScrollView(
            padding: const EdgeInsets.symmetric(horizontal: 16.0),
            child: Column(
              children: [
                SizedBox(
                  height: 375, // Increase the height of the card
                  child: Card(
                    elevation: 5,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                    child: Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: BarChart(
                        BarChartData(
                          alignment: BarChartAlignment.spaceAround,
                          maxY: maxY,
                          titlesData: FlTitlesData(
                            leftTitles: AxisTitles(
                              sideTitles: SideTitles(showTitles: false),
                            ),
                            rightTitles: AxisTitles(
                              sideTitles: SideTitles(
                                showTitles: true,
                                getTitlesWidget: (value, meta) {
                                  return Text(
                                    formatValue(value),
                                    style: const TextStyle(fontSize: 12),
                                  );
                                },
                                reservedSize: 40,
                              ),
                            ),
                            bottomTitles: AxisTitles(
                              sideTitles: SideTitles(
                                showTitles: true,
                                reservedSize: 65, // Adjust spacing
                                getTitlesWidget: (value, meta) {
                                  String cryptoName = cryptoData[value.toInt()]['nom'];
                                  return RotatedBox(
                                    quarterTurns: 1, // Rotate text vertically
                                    child: Text(
                                      cryptoName,
                                      style: const TextStyle(fontSize: 12),
                                    ),
                                  );
                                },
                              ),
                            ),
                          ),
                          borderData: FlBorderData(show: false),
                          barGroups: cryptoData
                              .asMap()
                              .entries
                              .map(
                                (entry) =>
                                BarChartGroupData(
                                  x: entry.key,
                                  barRods: [
                                    BarChartRodData(
                                      toY: entry.value["valeur"].toDouble(),
                                      color: Colors.blue,
                                      width: 15,
                                      borderRadius: BorderRadius.circular(4),
                                    ),
                                  ],
                                ),
                          )
                              .toList(),
                          gridData: FlGridData(
                            show: true,
                            drawHorizontalLine: true,
                            getDrawingHorizontalLine: (value) {
                              double roundedValue = (value / 5)
                                  .roundToDouble() * 5;
                              return FlLine(
                                color: Colors.black.withOpacity(0.2),
                                strokeWidth: 0.8,
                                dashArray: [5, 5],
                              );
                            },
                          ),
                        ),
                      ),
                    ),
                  ),
                ),
                const SizedBox(height: 16),
                ...cryptoData.map((crypto) {
                  bool isFavorite = favorisStatus[crypto["id_cryptomonaie"]] ?? false;
                  return Card(
                    margin: const EdgeInsets.symmetric(vertical: 8),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                    elevation: 5,
                    child: ListTile(
                      contentPadding: const EdgeInsets.all(16.0),
                      title: Text(
                        crypto['nom'],
                        style: const TextStyle(
                          fontSize: 18,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      subtitle: Text(
                        'Value: ${crypto['valeur']}',
                        style: const TextStyle(fontSize: 14),
                      ),
                      trailing: IconButton(
                        icon: Icon(
                          isFavorite ? Icons.favorite : Icons.favorite_border,
                          color: isFavorite ? Colors.red : Colors.grey,
                        ),
                        onPressed: () =>
                            toggleFavori(crypto["id_cryptomonaie"], isFavorite),
                      ),
                    ),
                  );
                }).toList(),
              ],
            ),
          );
        },
      ),
    );
  }
  void toggleFavori(int idCryptomonnaie, bool isFavorite) async {
    if (isFavorite) {
      await _favorisService.supprimerFavori(
          idCryptomonnaie, widget.idUtilisateur);
      setState(() {
        favorisStatus[idCryptomonnaie] = false;
      });
      print("Favori supprimé avec succès");
    } else {
      await _favorisService.ajouterFavori(
          idCryptomonnaie, widget.idUtilisateur);
      setState(() {
        favorisStatus[idCryptomonnaie] = true;
      });
      print("Favori ajouté avec succès");
    }
  }

  String formatValue(double value) {
    return value > 1000 ? '${(value / 1000).toStringAsFixed(1)}K' : value
        .toStringAsFixed(0);
  }

  double getMaxY(List<Map<String, dynamic>> cryptoData) {
    double maxValue = 0;
    if (cryptoData.isNotEmpty) {
      maxValue = cryptoData
          .map((crypto) => crypto['valeur'])
          .reduce((a, b) => a > b ? a : b)
          .toDouble();
    }
    return (maxValue / 5).ceilToDouble() * 5;
  }
}