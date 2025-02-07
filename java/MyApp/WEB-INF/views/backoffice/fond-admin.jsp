<%@ page import="java.util.*" %>
<%@ page import="model.transaction.*" %>
<%@ include file="/header-admin.jsp" %>

<div class="title-group mb-3">
    <h1 class="h2 mb-0">Validation des Fonds</h1>
</div>

<div class="row my-4">
    <div class="col-lg-12 col-12">
        <div class="custom-block bg-white">
            <h5 class="mb-4">Fonds en attente</h5>
            <div class="table-responsive">
                <table class="account-table table">
                    <thead>
                        <tr>
                            <th scope="col">Date</th>
                            <th scope="col">Montant</th>
                            <th scope="col">Utilisateur</th>
                            <th scope="col">etat</th>
                            <th scope="col">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Fond> fonds = (List<Fond>) request.getAttribute("fonds");
                            if (fonds != null) {
                                for (Fond fond : fonds) {
                                    if (fond.getEtat() != 1) { // Afficher uniquement ceux dont l'état n'est pas accepté
                                        String etat;
                                        switch (fond.getEtat()) {
                                            case 0:
                                                etat = "En attente";
                                                break;
                                            case 2:
                                                etat = "Refuse";
                                                break;
                                            default:
                                                etat = "Inconnu";
                                                break;
                                        }
                        %>
                            <tr>
                                <td><%= fond.getDateFond() %></td>
                                <td><%= fond.getValeurFond() %> Ar</td>
                                <td><%= fond.getIdUtilisateur() %></td>
                                <td><%= etat %></td>
                                <td>
                                    <form action="fondadmin" method="post" style="display:inline;">
                                        <input type="hidden" name="idfond" value="<%= fond.getIdFond() %>">
                                        <input type="hidden" name="operation" value="accepter">
                                        <button type="submit" class="btn btn-success">Accepter</button>
                                    </form>
                                    <form action="fondadmin" method="post" style="display:inline;">
                                        <input type="hidden" name="idfond" value="<%= fond.getIdFond() %>">
                                        <input type="hidden" name="operation" value="refuser">
                                        <button type="submit" class="btn btn-danger">Refuser</button>
                                    </form>
                                </td>
                            </tr>
                        <%
                                    }
                                }
                            } else {
                        %>
                            <tr>
                                <td colspan="5">Aucun fond en attente.</td>
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
