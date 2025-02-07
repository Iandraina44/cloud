 <%@ page import="java.util.*" %>
<%@ page import="model.crypto.*" %>
<%@ page import="model.transaction.*" %>
<%@ page import="model.role.*" %>

<%@ include file="/header.jsp" %>

<div class="title-group mb-3">
    <h1 class="h2 mb-0"></h1>
</div>
  

<div class="row my-4">
    <div class="col-lg-12 col-12">
        <div class="custom-block bg-white">
            <h5 class="mb-4">Operations Effectues</h5>


            <div class="custom-block bg-white">
            <form class="custom-form search-form" action="operationHistorique" method="post" role="form">
                <div class="row">
                   

                    <div class="col-lg-6 col-md-4 col-12">
                        <select class="form-control mb-lg-0 mb-md-0" name="cryptomonaie">
                             <option value="-1">Cryptomonaie</option>
                            <%
                                List<Cryptomonaie> cryptomonaies = (List<Cryptomonaie>) request.getAttribute("cryptomonaies");
                                for (Cryptomonaie crypto : cryptomonaies) {
                            %>
                                <option value="<%= crypto.getId() %>"><%= crypto.getNom() %></option>
                            <%
                                }
                            %>
                        </select>
                    </div>


                     <div class="col-lg-6 col-md-4 col-12">
                        <select class="form-control mb-lg-0 mb-md-0" name="utilisateur">
                             <option value="-1">Utilisateur</option>

                            <%
                                List<Utilisateur> utilisateurs = (List<Utilisateur>) request.getAttribute("utilisateurs");
                                for (Utilisateur user : utilisateurs) {
                            %>
                                <option value="<%= user.getIdUtilisateur() %>"> Utilisateur<%= user.getIdUtilisateur() %></option>
                            <%
                                }
                            %>
                        </select>
                    </div>



                        <div class="col-lg-6 col-md-4 col-12" style="margin-top:2%">
                            <input class="form-control mb-lg-0 mb-md-0" type="datetime-local" name="date_min" >
                        </div>

                        <!-- Selection de la date maximum -->
                        <div class="col-lg-6 col-md-4 col-12"  style="margin-top:2%">
                            <input class="form-control mb-lg-0 mb-md-0" type="datetime-local" name="date_max" >
                        </div>

                    <div class="col-lg-4 col-md-4 col-12"  style="margin-top:2%">
                        <button type="submit" class="form-control">Filtrer</button>
                    </div>
                </div>
            </form>
        </div>

            <div class="table-responsive">
                <table class="account-table table">
                    <thead>
                        <tr>
                            <th scope="col">Utilisateur</th>
                            <th scope="col"></th>


                            <th scope="col">cryptomonnaie</th>

                            <th scope="col">Prix Unitaire</th>

                            <th scope="col">Quantite</th>

                            <th scope="col">Date</th>

                            <th scope="col">Commission</th>

                            <th scope="col">Operations</th>

                        </tr>
                    </thead>

                    <tbody>
                     <% 
                        List<AchatCrypto> achats = (List<AchatCrypto>) request.getAttribute("achats");
                        if (achats != null && !achats.isEmpty()) {
                            for (AchatCrypto achat : achats) {
                        %>

                            <tr>
                                <td scope="row"> 
                                    <div class="d-flex align-items-center">
                                        <a href="javascript:void(0);" onclick="showHistory(<%= achat.getUtilisateur().getIdUtilisateur() %>)">
                                            <img src="assets/images/<%= achat.getUtilisateur().getImage() %>" class="profile-image img-fluid" alt="">
                                        </a>
                                    </div>
                                </td>
                                <td scope="row">Utilisateur <%= achat.getUtilisateur().getIdUtilisateur() %></td>

                              

                                <td scope="row"> <%= achat.getNomCrypto()%> - ID<%= achat.getIdCryptomonaie()%></td>

                                <td scope="row"><%=  achat.getPrixUnitaire() %></td>

                                <td scope="row"> <%=achat.getQuantite()  %></td>

                                <td scope="row"><%= achat.getDateAchat() %></td>

                                <td scope="row"> <%=achat.getPrixWCommission()  %> </td>

                                <td scope="row">
                                    <span class="badge text-bg-success">
                                        Achat
                                    </span>
                                </td>
                            </tr>
                        <%}}%>


                         <% 
          
                        List<Vente> ventes = (List<Vente>) request.getAttribute("ventes");
                        if (ventes != null) {
                            for (Vente vente : ventes) {
                        %>

                          <tr>
                                <td scope="row">
                                    <div class="d-flex align-items-center">
                                        <a href="javascript:void(0);" onclick="showHistory(<%= vente.getUtilisateur().getIdUtilisateur() %>)">
                                            <img src="assets/images/<%= vente.getUtilisateur().getImage() %>" class="profile-image img-fluid" alt="">
                                        </a>
                                    </div>
                                </td>
                                <td scope="row">Utilisateur <%= vente.getUtilisateur().getIdUtilisateur() %></td>

                              

                                <td scope="row" class="text-muted"> <%= vente.getNomCrypto()%> - ID<%= vente.getIdCryptomonaie()%></td>

                                <td scope="row"><%=  vente.getPrixUnitaire() %></td>

                                <td scope="row"> <%=vente.getQuantite()  %></td>

                                <td scope="row"><%= vente.getDateVente() %></td>

                                <td scope="row"> <%=vente.getPrixWCommission()  %> </td>

                                <td scope="row">
                                    <span class="badge text-bg-danger">
                                        Vente
                                    </span>
                                </td>
                            </tr>
                        <%}}%>
                    </tbody>
                </table>
            </div>

         </div>




         
<!-- Modal pour afficher l'historique -->
<div id="historyModal" class="modal" tabindex="-1" aria-labelledby="historyModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-xl"> <!-- Ajout de modal-xl pour une grande taille -->
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="historyModalLabel">Historique des Operations</h5>

        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <div class="d-flex flex-wrap align-items-center border-bottom pb-3 mb-3" >
        
        <div class="d-flex align-items-center" style="margin-left:10%; margin-top:2%" >
            <img  id="image" src=""  style="width:10%"   alt="">               

        <div style="margin-left:2%">
            <p><small id="user" class="text-muted"></small></p>
            <p><small id="mail" class="text-muted"></small></p>
        </div>
        </div>


      
    </div>

      <div class="modal-body" id="historyContent">

       
        <div class="table-responsive" >

            <table border="1" class="account-table table" id="tableau"></table>
        </div>
        
      </div>
    </div>
  </div>
</div>


    
    </div>
</div>



<script>
function showHistory(idUtilisateur) {
    // Requête AJAX pour récupérer l'historique des achats et des ventes
    fetch('http://localhost:8080/MyApp/operationUtilisateur?id=' + idUtilisateur)
        .then(response => response.json())
        .then(data => {
            
            // Accéder aux achats et ventes depuis la réponse JSON
            let achats = data.achats;
            let ventes = data.ventes;
             console.log(data);
             console.log(achats[0].quantite);


             const image = document.getElementById("image");
             image.src="assets/images/" + achats[0].utilisateur.image;


             const user = document.getElementById("user");
             user.textContent= "ID utilisateur "+  achats[0].utilisateur.idUtilisateur;


             const mail = document.getElementById("mail");
             mail.textContent= achats[0].utilisateur.email;





          const table = document.getElementsByTagName("table")[1]; // Récupérer le premier tableau
          table.innerHTML="";

          const thead = document.createElement("thead");

            var row = thead.insertRow(); // Créer une nouvelle ligne
            var colonne1 = document.createElement("th"); // Créer une cellule
            colonne1.textContent = "Crypto"; // Ajouter le texte
            colonne1.setAttribute("scope", "col");
            row.appendChild(colonne1); // Ajouter la cellule à la ligne


            var colonne2 = document.createElement("th"); // Créer une cellule
            colonne2.textContent = "Prix Unitaire"; // Ajouter le texte
            colonne2.setAttribute("scope", "col");
            row.appendChild(colonne2); // Ajouter la cellule à la ligne


            var colonne3 = document.createElement("th"); // Créer une cellule
            colonne3.textContent = "quantite"; // Ajouter le texte
            colonne3.setAttribute("scope", "col");
            row.appendChild(colonne3); // Ajouter la cellule à la ligne



            var colonne4 = document.createElement("th"); // Créer une cellule
            colonne4.textContent = "date"; // Ajouter le texte
            colonne4.setAttribute("scope", "col");
            row.appendChild(colonne4); // Ajouter la cellule à la ligne


            
            var colonne5 = document.createElement("th"); // Créer une cellule
            colonne5.textContent = "commission"; // Ajouter le texte
            colonne5.setAttribute("scope", "col");
            row.appendChild(colonne5); // Ajouter la cellule à la ligne



            var colonne6 = document.createElement("th"); // Créer une cellule
            colonne6.textContent = "operation"; // Ajouter le texte
            colonne6.setAttribute("scope", "col");
            row.appendChild(colonne6); // Ajouter la cellule à la ligne

           
            thead.appendChild(row); // Ajouter la ligne au tableau
            table.appendChild(thead); // Ajouter la ligne au tableau



          const tbody = document.createElement("tbody");

            achats.forEach(achat => {
                var row2 = tbody.insertRow(); // Créer une nouvelle ligne
                var td1 = document.createElement("td"); // Créer une cellule
                td1.textContent = achat.nomCrypto; // Ajouter le texte
                row2.appendChild(td1); // Ajouter la cellule à la ligne


                var td2 = document.createElement("td"); // Créer une cellule
                td2.textContent = achat.prixUnitaire; // Ajouter le texte
                row2.appendChild(td2); // Ajouter la cellule à la ligne


                var td3 = document.createElement("td"); // Créer une cellule
                td3.textContent = achat.quantite;// Ajouter le texte
                row2.appendChild(td3); // Ajouter la cellule à la ligne



                var td4 = document.createElement("td"); // Créer une cellule
                td4.textContent = achat.dateAchat; // Ajouter le texte
                row2.appendChild(td4); // Ajouter la cellule à la ligne


            
                var td5 = document.createElement("td"); // Créer une cellule
                td5.textContent = achat.prixWCommission;; // Ajouter le texte
                row2.appendChild(td5); // Ajouter la cellule à la ligne



                var td6 = document.createElement("td"); // Créer une cellule
                td6.textContent = "Achat"; // Ajouter le texte
                row2.appendChild(td6); // Ajouter la cellule à la ligne

           
                tbody.appendChild(row2); // Ajouter la ligne au tableau
 

            });



           ventes.forEach(vente => {
                var row2 = tbody.insertRow(); // Créer une nouvelle ligne
                var td1 = document.createElement("td"); // Créer une cellule
                td1.textContent = vente.nomCrypto; // Ajouter le texte
                row2.appendChild(td1); // Ajouter la cellule à la ligne


                var td2 = document.createElement("td"); // Créer une cellule
                td2.textContent = vente.prixUnitaire; // Ajouter le texte
                row2.appendChild(td2); // Ajouter la cellule à la ligne


                var td3 = document.createElement("td"); // Créer une cellule
                td3.textContent = vente.quantite;// Ajouter le texte
                row2.appendChild(td3); // Ajouter la cellule à la ligne



                var td4 = document.createElement("td"); // Créer une cellule
                td4.textContent = vente.dateVente; // Ajouter le texte
                row2.appendChild(td4); // Ajouter la cellule à la ligne


            
                var td5 = document.createElement("td"); // Créer une cellule
                td5.textContent = vente.prixWCommission;; // Ajouter le texte
                row2.appendChild(td5); // Ajouter la cellule à la ligne



                var td6 = document.createElement("td"); // Créer une cellule
                td6.textContent = "vente"; // Ajouter le texte
                row2.appendChild(td6); // Ajouter la cellule à la ligne

           
                tbody.appendChild(row2); // Ajouter la ligne au tableau
 

            });

                table.appendChild(tbody); // Ajouter la ligne au tableau





            

            

          

             // Générer le contenu HTML pour les ventes

            let myModal = new bootstrap.Modal(document.getElementById('historyModal'));
            myModal.show();

           
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
</script>



<%@ include file="/footer.jsp" %>                            