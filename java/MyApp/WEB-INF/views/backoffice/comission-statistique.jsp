<%@ page import="java.util.*" %>
<%@ page import="model.transaction.*" %>
<%@ page import="model.crypto.*" %>
<%@ include file="/header-admin.jsp" %>


<div class="title-group mb-3">
    <h1 class="h2 mb-0">Analyse des Commissions</h1>
</div>

<div class="row my-4">
    <!-- Formulaire pour l'analyse des commissions -->
    <div class="col-lg-12 col-12">
        <div class="custom-block bg-white">
            <form class="custom-form search-form" action="analyseCommission" method="post" role="form">
                <div class="row">
                    <div class="col-lg-12 mb-2">
                        <h6>Analyse des Commissions</h6>
                    </div>

                    <!-- Selection du type d'analyse -->
                    <div class="col-lg-4 col-md-4 col-12">
                        <select class="form-control mb-lg-0 mb-md-0" name="type_analyse" required>
                            <option value="Somme">Somme</option>
                            <option value="Moyenne">Moyenne</option>
                        </select>
                    </div>



                    <!-- Selection du type de commission -->
                    <div class="col-lg-4 col-md-4 col-12">
                        <select class="form-control mb-lg-0 mb-md-0" name="type_commission" required>
                            <option value="achat">Achat</option>
                            <option value="vente">Vente</option>
                        </select>
                    </div>


                    <div class="col-lg-4 col-md-4 col-12">
                        <select class="form-control mb-lg-0 mb-md-0" name="crypto">
                        <option value="">Tous</option>
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
                    <%-- </div> --%>


                  
                    <%-- <div class="row"> --%>
                    <!-- Selection de la date minimum -->
                        <div class="col-lg-4 col-md-4 col-12">
                            <input class="form-control mb-lg-0 mb-md-0" type="datetime-local" name="date_min" style="margin-top:3%">
                        </div>

                        <!-- Selection de la date maximum -->
                        <div class="col-lg-4 col-md-4 col-12">
                            <input class="form-control mb-lg-0 mb-md-0" type="datetime-local" name="date_max" style="margin-top:3%">
                        </div>

                    <!-- Bouton pour valider l'analyse -->
                        <div class="col-lg-4 col-md-4 col-12" >
                            <button type="submit" class="form-control" style="margin-top:2%">Valider</button>
                        </div>
                <%-- </div> --%>
                </div>

            </form>
        </div>
    </div>


    <!-- Affichage du resultat de l'analyse -->
    <div class="col-lg-12 col-12">
        <div class="custom-block bg-white">
            <h5 class="mb-4">Resultat de l'Analyse</h5>
            <div class="table-responsive">
                <table class="account-table table">
                    <thead>
                        <tr>
                            <th scope="col">Resultat</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <%= request.getAttribute("result") != null ? request.getAttribute("result") : "Aucun resultat" %>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</div>

<%@ include file="/footer.jsp" %>
