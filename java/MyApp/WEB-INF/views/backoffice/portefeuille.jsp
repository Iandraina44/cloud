<%@ page import="java.util.*" %>
<%@ page import="model.crypto.*" %>
<%@ page import="model.transaction.*" %>
<%@ include file="/header.jsp" %>

<div class="title-group mb-3">
    <h1 class="h2 mb-0">Portefeuille Crypto</h1>
</div>

<div class="row my-4">
    <!-- Formulaire pour filtrer par date -->
    <div class="col-lg-12 col-12">
        <div class="custom-block bg-white">
            <form class="custom-form search-form" action="adminportefeuille" method="post">
                <div class="row">
                    <div class="col-lg-12 mb-2">
                        <h6>Filtrer par date et heure</h6>
                    </div>

                    <div class="col-lg-4 col-md-4 col-12">
                        <label for="dateMax">Sélectionner une date :</label>
                        <input class="form-control" type="datetime-local" id="dateMax" name="dateMax" required>
                    </div>

                    <div class="col-lg-4 col-md-4 col-12" style="margin-top:3%" >
                        <button type="submit" class="form-control">Afficher le portefeuille</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- Affichage du portefeuille (historique des transactions) -->
    <div class="col-lg-12 col-12">
        <div class="custom-block bg-white">
            <h5 class="mb-4">Portefeuille</h5>
            <div class="table-responsive">
                <table class="account-table table">
                    <thead>
                        <tr>
                            <th scope="col">Utilisateur</th>
                            <th scope="col">Total Achat</th>
                            <th scope="col">Total Vente</th>
                            <th scope="col">Valeur Portefeuille</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<PortefeuilleAlea> portefeuilles = (List<PortefeuilleAlea>) request.getAttribute("portefeuilles");
                            if (portefeuilles != null) {
                            for (PortefeuilleAlea portefeuille : portefeuilles) {
                        %>
                            <tr>
                                <td><%= portefeuille.getIdUtilisateur() %></td>
                                <td><%= portefeuille.getTotalAchat() %></td>
                                <td><%= portefeuille.getTotalVente() %></td>
                                <td><%= portefeuille.getValeurPortefeuille() %></td>
                            </tr>
                        <% 
                            }
                            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<%@ include file="/footer.jsp" %>
