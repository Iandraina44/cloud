<%@ include file="/header.jsp" %>
<%@ page import="java.util.* , java.sql.* , model.crypto.* ,model.transaction.* , java.time.LocalDateTime, java.time.format.DateTimeFormatter" %>

<div class="title-group mb-3">
    <h1 class="h2 mb-0">Statistiques des Cryptomonnaies</h1>
</div>

<div class="row my-4">
    <div class="col-lg-12 col-12">
        <div class="custom-block bg-white">
            <form class="custom-form search-form" action="statistiquesCrypto" method="post" role="form">
                <div class="row">
                    

                    <div class="col-lg-4 col-md-4 col-12">
                        <label for="dateMin">Date et Heure Min:</label>
                        <input class="form-control" type="datetime-local" id="dateMin" name="dateMin" required>
                    </div>

                    <div class="col-lg-4 col-md-4 col-12">
                        <label for="dateMax">Date et Heure Max:</label>
                        <input class="form-control" type="datetime-local" id="dateMax" name="dateMax" required>
                    </div>

                    <div class="col-lg-4 col-md-4 col-12">
                       <label for="analyse">Type d'analyse:</label>
                        <select class="form-control" name="analyse">
                            <option value="q1">choisir une analyse</option>
                            <option value="q1">Premier Quartile (Q1)</option>
                            <option value="max">Max</option>
                            <option value="min">Min</option>
                            <option value="moyenne">Moyenne</option>
                            <option value="ecart_type">Écart-Type</option>
                        </select>
                    </div>


                    <div class="col-lg-12 col-md-4 col-12">
                        <label for="crypto">Choisir une Cryptomonnaie:</label>
                        <% 
                            List<Cryptomonaie> cryptomonaies = (List<Cryptomonaie>) request.getAttribute("cryptomonaies");
                            if (cryptomonaies != null) {
                                for (Cryptomonaie crypto : cryptomonaies) {
                        %>
                            <div>
                                <input type="checkbox" name="crypto" value="<%= crypto.getId() %>"> <%= crypto.getNom() %>
                            </div>
                        <%
                                }
                            }
                        %>
                    </div>

                    <div class="col-lg-4 col-md-4 col-12 mt-3">
                        <button type="submit" class="form-control btn btn-primary">Valider</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

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
                    <% 
                        List<String> resultats = (List<String>) request.getAttribute("resultats");
                        if (resultats != null) {
                            for (String resultat : resultats) {
                    %>

                        <tr>
                            <td>
                                <%= resultat %>
                            </td>
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

    <%-- <% 
        List<String> resultats = (List<String>) request.getAttribute("resultats");
        if (resultats != null) {
            for (String resultat : resultats) {
    %>



        <div class="col-lg-12 col-12 mt-4">
            <div class="custom-block bg-light">
                <h5><%= resultat %></h5>
            </div>
        </div>
    <%
            }
        }
    %> --%>
</div>
<script>
    function setDefaultDateTime() {
    let now = new Date();
    let minDate = new Date(now.getTime() - 30000); // 30 secondes de moins

    function formatDateTime(date) {
        let year = date.getFullYear();
        let month = String(date.getMonth() + 1).padStart(2, '0');
        let day = String(date.getDate()).padStart(2, '0');
        let hours = String(date.getHours()).padStart(2, '0');
        let minutes = String(date.getMinutes()).padStart(2, '0');

        return `${year}-${month}-${day}T${hours}:${minutes}`;
    }

    document.getElementById('dateMin').value = formatDateTime(minDate);
    document.getElementById('dateMax').value = formatDateTime(now);
}

window.onload = setDefaultDateTime;

</script>

<%@ include file="/footer.jsp" %>
