<%@ page import="java.util.*" %>
<%@ page import="model.transaction.*" %>
<%@ include file="/header.jsp" %>

<div class="title-group mb-3">
    <h1 class="h2 mb-0">Gestion des Fonds</h1>
</div>

<div class="row my-4">
    <!-- Formulaire pour ajouter un fond (Depot ou Retrait) -->
    <div class="col-lg-12 col-12">
        <div class="custom-block bg-white">
            <form class="custom-form search-form" action="fond" method="post" role="form">
                <div class="row">
                    <div class="col-lg-12 mb-2">
                        <h6>Ajouter un Fond</h6>
                    </div>

                    <!-- Montant -->
                    <div class="col-lg-3 col-md-3 col-12">
                        <input class="form-control mb-lg-0 mb-md-0" name="montant" type="number" placeholder="Montant" required>
                    </div>

                    <!-- Selection du type d'operation -->
                    <div class="col-lg-3 col-md-3 col-12">
                        <select class="form-control mb-lg-0 mb-md-0" name="operation">
                            <option value="depot">Depot</option>
                            <option value="retrait">Retrait</option>
                        </select>
                    </div>

                    <!-- Date -->
                    <div class="col-lg-3 col-md-3 col-12">
                        <input class="form-control mb-lg-0 mb-md-0" name="date" type="date" required>
                    </div>

                    <!-- Bouton -->
                    <div class="col-lg-12 mt-3">
                        <button type="submit" class="form-control">Valider</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- Affichage du fond total -->
    <div class="col-lg-12 col-12">
        <div class="custom-block custom-block-balance">
            <small>Fond total:</small>
            <h2 class="mt-2 mb-3"><%= request.getAttribute("fondTotal") %></h2>
        </div>
    </div>

    <!-- Historique des fonds -->
    <div class="col-lg-12 col-12">
        <div class="custom-block bg-white">
            <h5 class="mb-4">Historique des Fonds</h5>
            <div class="table-responsive">
                <table class="account-table table">
                    <thead>
                        <tr>
                            <th scope="col">Date</th>
                            <th scope="col">Montant</th>
                            <th scope="col">Operation</th>
                            <th scope="col">etat</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Fond> fonds = (List<Fond>) request.getAttribute("fonds");
                            for (Fond fond : fonds) {
                                String operation = (fond.getValeurFond() >= 0) ? "Depot" : "Retrait";
                                String etat;
                                String couleurEtat;

                                switch (fond.getEtat()) {
                                    case 0:
                                        etat = "En attente";
                                        couleurEtat = "#A8DADC";
                                        break;
                                    case 1:
                                        etat = "Accepte";
                                        couleurEtat = "green";
                                        break;
                                    case 2:
                                        etat = "Refuse";
                                        couleurEtat = "red";
                                        break;
                                    default:
                                        etat = "Inconnu";
                                        couleurEtat = "black";
                                        break;
                                }
                        %>
                            <tr>
                                <td><%= fond.getDateFond() %></td>
                                <td style="color: <%= (fond.getValeurFond() >= 0) ? "green" : "red" %>;">
                                    <%= fond.getValeurFond() %>
                                </td>
                                <td><%= operation %></td>
                                <td style="color: <%= couleurEtat %>;"><%= etat %></td>
                            </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<%@ include file="/footer.jsp" %>
