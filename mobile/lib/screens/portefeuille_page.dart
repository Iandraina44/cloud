import 'package:flutter/material.dart';
import '../services/portefeuille_service.dart';

class PortefeuillePage extends StatefulWidget {
  final int userId; // On attend un int

  const PortefeuillePage({Key? key, required this.userId}) : super(key: key);

  @override
  _PortefeuillePageState createState() => _PortefeuillePageState();
}

class _PortefeuillePageState extends State<PortefeuillePage> {
  final PortefeuilleService _portefeuilleService = PortefeuilleService();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Mon Portefeuille'),
      ),
      body: StreamBuilder<List<Portefeuille>>(
        stream: _portefeuilleService.getPortefeuilleByUserStream(widget.userId),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }
          if (snapshot.hasError) {
            return Center(child: Text('Erreur : ${snapshot.error}'));
          }
          if (!snapshot.hasData || snapshot.data!.isEmpty) {
            return const Center(child: Text('Aucun portefeuille trouvé.'));
          }

          List<Portefeuille> portefeuilleList = snapshot.data!;

          return SingleChildScrollView(
            scrollDirection: Axis.horizontal,
            child: DataTable(
              columns: const [
                DataColumn(label: Text('Nom Crypto')),
                DataColumn(label: Text('Quantité')),
              ],
              rows: portefeuilleList.map((portefeuille) {
                return DataRow(
                  cells: [
                    DataCell(Text(portefeuille.nomCrypto)),
                    DataCell(Text(portefeuille.quantite.toString())),
                  ],
                );
              }).toList(),
            ),
          );
        },
      ),
    );
  }
}
