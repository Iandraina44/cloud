<%@ page import="java.util.*" %>
<%@ page import="model.transaction.*" %>
<%@ page import="model.crypto.*" %>
<%@ include file="/header-admin.jsp" %>

<div class="title-group mb-3">
    <h1 class="h2 mb-0">Commission</h1>
</div>

<div class="row my-4">
    <!-- Formulaire pour vendre la cryptomonnaie -->
    <div class="col-lg-12 col-12">
        <div class="custom-block bg-white">
            <form class="custom-form search-form" action="commission" method="post" role="form">
                <div class="row">
                    <div class="col-lg-12 mb-2">
                        <h6>Modifier Commission</h6>
                    </div>    
 
                    <div class="col-lg-4 col-md-4 col-12">
                        <input class="form-control mb-lg-0 mb-md-0"  type="datetime-local" name="dateModification" required >
                    </div>

                    <div class="col-lg-4 col-md-4 col-12">
                        <select class="form-control mb-lg-0 mb-md-0" name="typeCommission">
                             <option value="vente">vente</option>
                             <option value="achat">achat</option>
                           
                        </select>
                    </div>
                    <div class="col-lg-4 col-md-4 col-12">
                        <input class="form-control mb-lg-0 mb-md-0" type="number" step="0.01" name="valeur" placeholder="valeur (%)" required>
                    </div>

                    <div class="col-lg-4 col-md-4 col-12" style="margin-top:3%">
                        <button type="submit" class="form-control">Modifier</button>
                    </div>
                </div>
            </form>
        </div>
    </div>


    <%-- <!-- Affichage des fonds totaux -->
    <div class="col-lg-12 col-12">
        <div class="custom-block custom-block-balance">
            <small></small>
            <h2 class="mt-2 mb-3"><%= request.getAttribute("fond") %></h2>
        </div>
    </div> --%>


     <div class="col-lg-12 col-12">
        <c:if test="${not empty message}">
            <div class="alert alert-info">
                ${message}
            </div>
        </c:if>
    </div>

    <!-- Affichage du portefeuille (historique des ventes) -->
    <div class="col-lg-12 col-12">
        <div class="custom-block bg-white">
            <h5 class="mb-4">Commission Actuelle</h5>
            <div class="table-responsive">
                <table class="account-table table">
                    <thead>
                        <tr>
                            <th scope="col">type de Commission</th>
                            <th scope="col">ID Commission</th>
                            <th scope="col">valeur (%)</th>
                            <th scope="col">date de Modification</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            Commission actuelVente = (Commission) request.getAttribute("actuelVente");
                            Commission actuelAchat = (Commission) request.getAttribute("actuelAchat");
                            
                        %>
                            <tr>
                                <td><%= actuelVente.getTypeCommission() %></td>
                                <td><%= actuelVente.getIdCommission() %></td>
                                <td><%= actuelVente.getValeur() %></td>
                                <td><%= actuelVente.getDateModification() %></td>

                            </tr>

                            <tr>
                                <td><%= actuelAchat.getTypeCommission() %></td>
                                <td><%= actuelAchat.getIdCommission() %></td>
                                <td><%= actuelAchat.getValeur() %></td>
                                <td><%= actuelAchat.getDateModification() %></td>

                            </tr>


                      
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<%@ include file="/footer.jsp" %>
