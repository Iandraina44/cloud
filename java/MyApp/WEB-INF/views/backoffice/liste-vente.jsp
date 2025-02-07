 <%@ page import="java.util.*" %>
<%@ page import="model.crypto.*" %>
<%@ include file="/header-admin.jsp" %>

<div class="title-group mb-3">
    <h1 class="h2 mb-0"></h1>
</div>

<div class="row my-4">
    <!-- Formulaire pour vendre la cryptomonnaie -->
   <div class="col-lg-8 col-12 mx-auto">
 
        <div class="custom-block custom-block-transations">
            <h5 class="mb-4">Historique des ventes</h5>


            <% 
          
            List<Vente> ventes = (List<Vente>) request.getAttribute("ventes");
            if (ventes != null) {
                for (Vente vente : ventes) {
            %>

                <div class="d-flex flex-wrap align-items-center mb-4">

                    <div class="d-flex align-items-center">
                        <img src="assets/images/crypto1.jpg" class="profile-image img-fluid" alt="">

                        <div>
                            <p>
                            <%= vente.getNomCrypto()%> - ID<%= vente.getIdCryptomonaie()%>
                            </p>

                            <small class="text-muted"> Utilisateur <%= vente.getUtilisateur().getIdUtilisateur() %></small>
                        </div>
                    </div>

                    <div class="ms-auto">
                        <small><%= vente.getDateVente() %></small>
                        <p>Prix  <%=  vente.getPrixUnitaire() %></p>
                       
                       
                    </div>


                    <div class="ms-auto">
                       <small class="text-muted">Quantite:</small>
                         <strong class="d-block text-danger"><span span class="me-1" >  <%=vente.getQuantite()  %> </span></strong>
                       
                    </div>


                    <div class="ms-auto">
                        <small class="text-muted">commission:</small>
                            <strong class="d-block text-success"><span span class="me-1" > <%= vente.getPrixWCommission()  %> </span></strong>
                        
                    </div>
                </div>

            <%}}%>
        </div>

   
    </div>
</div>

<%@ include file="/footer.jsp" %>
        