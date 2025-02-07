<%@ page import="java.util.*" %>
<%@ page import="model.crypto.*" %>
<%@ page import="model.transaction.*" %>
<%@ include file="/header.jsp" %>

<div class="title-group mb-3">
    <h1 class="h2 mb-0">Vente</h1>
</div>

<div class="row my-4">
    <!-- Formulaire pour vendre la cryptomonnaie -->
    <div class="col-lg-12 col-12">
        <div class="custom-block bg-white">
            <form class="custom-form search-form" action="vente" method="post" role="form">
                <div class="row">
                    <div class="col-lg-12 mb-2">
                        <h6>Vente</h6>
                    </div>

                    <div class="col-lg-4 col-md-4 col-12">
                        <select class="form-control mb-lg-0 mb-md-0" name="cryptomonaie">
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
                    <div class="col-lg-4 col-md-4 col-12">
                        <input class="form-control mb-lg-0 mb-md-0" name="quantite" type="number" placeholder="Quantite" required>
                    </div>

                    <div class="col-lg-4 col-md-4 col-12">
                        <button type="submit" class="form-control">Vendre</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- Affichage des fonds totaux -->
    <div class="col-lg-12 col-12">
        <div class="custom-block custom-block-balance">
            <small>Fond total:</small>
            <h2 class="mt-2 mb-3"><%= request.getAttribute("fond") %></h2>
        </div>
    </div>

    <!-- Affichage du portefeuille (historique des ventes) -->
    <div class="col-lg-12 col-12">
        <div class="custom-block bg-white">
            <h5 class="mb-4">Portefeuille</h5>
            <div class="table-responsive">
                <table class="account-table table">
                    <thead>
                        <tr>
                            <th scope="col">Crypto</th>
                            <th scope="col">Quantite</th>
                            <th scope="col">Prix unitaire</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Vente> ventes = (List<Vente>) request.getAttribute("ventes");
                            for (Vente vente : ventes) {
                        %>
                            <tr>
                                <td><%= vente.getNomCrypto() %></td>
                                <td><%= vente.getQuantite() %></td>
                                <td><%= vente.getPrixUnitaire() %></td>
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
