import 'package:flutter/material.dart';
import '../services/historique_transaction.dart';
import '../services/achat_service.dart';
import '../services/vente_service.dart';

class HistoriquePage extends StatefulWidget {
  final int userId;

  const HistoriquePage({Key? key, required this.userId}) : super(key: key);

  @override
  _HistoriquePageState createState() => _HistoriquePageState();
}

class _HistoriquePageState extends State<HistoriquePage> {
  String _selectedType = 'achat'; // Default value
  late Stream<List<HistoriqueTransaction>> _transactionStream;

  final AchatService _achatService = AchatService();
  final VenteService _venteService = VenteService();

  @override
  void initState() {
    super.initState();
    _updateTransactionStream();
  }

  void _updateTransactionStream() {
    setState(() {
      if (_selectedType == 'achat') {
        _transactionStream = _achatService.getAchatStream(widget.userId);
      } else {
        _transactionStream = _venteService.getVenteStream(widget.userId);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Historique des Transactions")),
      body: Column(
        children: [
          // Dropdown for selecting "achat" or "vente"
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: DropdownButton<String>(
              value: _selectedType,
              onChanged: (String? newValue) {
                if (newValue != null) {
                  setState(() {
                    _selectedType = newValue;
                    _updateTransactionStream();
                  });
                }
              },
              items: const [
                DropdownMenuItem(value: 'achat', child: Text("Historique Achats")),
                DropdownMenuItem(value: 'vente', child: Text("Historique Ventes")),
              ],
            ),
          ),

          // Transaction List
          Expanded(
            child: StreamBuilder<List<HistoriqueTransaction>>(
              stream: _transactionStream,
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.waiting) {
                  return const Center(child: CircularProgressIndicator());
                }
                if (snapshot.hasError) {
                  return Center(child: Text("Erreur : ${snapshot.error}"));
                }
                if (!snapshot.hasData || snapshot.data!.isEmpty) {
                  return const Center(child: Text("Aucune transaction trouvée."));
                }

                // Display transactions in a table
                return SingleChildScrollView(
                  scrollDirection: Axis.horizontal,
                  child: DataTable(
                    columnSpacing: 15.0,  // Add spacing between columns
                    columns: const [
                      DataColumn(label: Text("Nom Crypto")),
                      DataColumn(label: Text("Quantité")),
                      DataColumn(label: Text("Date")),
                    ],
                    rows: snapshot.data!.map((transaction) {
                      return DataRow(
                        cells: [
                          DataCell(Text(transaction.nomCryptomonaie)),
                          DataCell(Text(transaction.quantite.toString())),
                          DataCell(Text(transaction.dateTransaction.toLocal().toString())),
                        ],
                      );
                    }).toList(),
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
