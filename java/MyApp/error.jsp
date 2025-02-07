<%@ page import= "java.util.*" %>
<%@ page import= "model.*" %>

<%@ include file="header.jsp" %>

<div class="title-group mb-3">
    <h1 class="h2 mb-0">Erreur</h1>
    <p>Fonds suffisants : <%= request.getAttribute("ampy") %></p>
    <p>Quantité demandée : <%= request.getAttribute("qt") %></p>
</div>


<%@ include file="footer.jsp" %>
