import 'package:flutter/material.dart';
import '../services/fond_service.dart';

class FondPage extends StatefulWidget {
  final int idUtilisateur;

  // On reçoit l'ID de l'utilisateur via le constructeur
  const FondPage({super.key, required this.idUtilisateur});

  @override
  _FondPageState createState() => _FondPageState();
}

class _FondPageState extends State<FondPage> {
  final TextEditingController _valeurFondController = TextEditingController();
  String? _operation = 'retrait'; // Par défaut, c'est retrait

  // Instance du service FondService
  final FondService _fondService = FondService();

  // Fonction pour soumettre le formulaire
  void _soumettreOperation() {
    final valeurFond = double.tryParse(_valeurFondController.text);
    if (valeurFond != null) {
      // Ajouter l'opération dans Firestore
      _fondService.ajouterFond(
        valeurFond: valeurFond,
        operation: _operation!,
        idUtilisateur: widget.idUtilisateur,
      );

      // Réinitialiser le champ de saisie après l'ajout
      _valeurFondController.clear();

      // Afficher un SnackBar pour indiquer que l'opération a réussi
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text("Opération ajoutée avec succès!"),
          duration: Duration(seconds: 2), // Durée de l'affichage du SnackBar
        ),
      );

      print("Opération ajoutée avec succès");
    } else {
      print("Veuillez entrer une valeur valide");
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Page de Fond')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Liste déroulante pour choisir l'opération (retrait ou dépôt)
            DropdownButton<String>(
              value: _operation,
              items: <String>['retrait', 'depot'].map((String value) {
                return DropdownMenuItem<String>(value: value, child: Text(value));
              }).toList(),
              onChanged: (newValue) {
                setState(() {
                  _operation = newValue;
                });
              },
            ),
            SizedBox(height: 20),
            // Champ texte pour entrer la valeur
            TextField(
              controller: _valeurFondController,
              keyboardType: TextInputType.number,
              decoration: InputDecoration(
                labelText: 'Entrez la valeur du fond',
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 20),
            // Bouton pour soumettre
            ElevatedButton(
              onPressed: _soumettreOperation,
              child: Text('Ajouter l\'opération'),
            ),
          ],
        ),
      ),
    );
  }
}
