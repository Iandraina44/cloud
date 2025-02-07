<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <meta name="description" content="">
        <meta name="author" content="Tooplate">

        <title>Cryptomonaie S5</title>

        <!-- CSS FILES -->      
        <link rel="preconnect" href="https://fonts.googleapis.com">
        
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>

        <link href="https://fonts.googleapis.com/css2?family=Unbounded:wght@300;400;700&display=swap" rel="stylesheet">

        <link href="assets/css/bootstrap.min.css" rel="stylesheet">

        <link href="assets/css/bootstrap-icons.css" rel="stylesheet">

        <link href="assets/css/apexcharts.css" rel="stylesheet">

        <link href="assets/css/tooplate-mini-finance.css" rel="stylesheet">

    </head>
    <body>
        <header class="navbar sticky-top flex-md-nowrap">
            <div class="col-md-3 col-lg-3 me-0 px-3 fs-6">
                <a class="navbar-brand" href="admin-login.jsp">
                    <i class="bi-box">LOG AS ADMIN</i>
                  
                </a>
            </div>

            <button class="navbar-toggler position-absolute d-md-none collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#sidebarMenu" aria-controls="sidebarMenu" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

          
            <div class="navbar-nav me-lg-2">
                <div class="nav-item text-nowrap d-flex align-items-center">
                 

                    <div class="dropdown px-3">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <img src="assets/images/crypto.jpg" class="profile-image img-fluid" alt="">
                        </a>
                        <ul class="dropdown-menu bg-white shadow">
                            <li>
                                <div class="dropdown-menu-profile-thumb d-flex">
                                    <img src="assets/images/crypto.jpg" class="profile-image img-fluid me-3" alt="">
                                    <div class="d-flex flex-column">
                                        <small>Crypto</small>
                                        <a href="#">Crypto S5</a>
                                    </div>
                                  
                                </div>
                            </li>

                        </ul>
                    </div>
                </div>
            </div>
        </header>

      
       <div class="container-fluid">
            <div class="row">
                <main class="main-wrapper col-md-12 ms-sm-auto py-4 col-lg-12 px-md-4 border-start" >
                <center> <div class="title-group mb-3">
                    <h1 class="h2 mb-0">Authentification</h1>
                </div>
                </center>


                <div class="row my-4">
                    <center>
                    <div class="col-lg-7 col-4">
                        <div class="custom-block bg-white">
                            <ul class="nav nav-tabs" id="myTab" role="tablist">
                                <li class="nav-item" role="presentation">
                                    <button class="nav-link active" id="profile-tab" data-bs-toggle="tab" data-bs-target="#profile-tab-pane" type="button" role="tab" aria-controls="profile-tab-pane" aria-selected="true">Inscription</button>
                                </li>

                                <li class="nav-item" role="presentation">
                                    <button class="nav-link" id="password-tab" data-bs-toggle="tab" data-bs-target="#password-tab-pane" type="button" role="tab" aria-controls="password-tab-pane" aria-selected="false">Connexion</button>
                                </li>

                            </ul>

                            <div class="tab-content" id="myTabContent">
                                <div class="tab-pane fade show active" id="profile-tab-pane" role="tabpanel" aria-labelledby="profile-tab" tabindex="0">
                                    <h6 class="mb-4">Inscrivez-Vous</h6>

                                    <form class="custom-form profile-form" id="inscriptionForm">
                                        <input class="form-control" type="email" id="email" placeholder="Johndoe@gmail.com" required>
                                        <input class="form-control" type="password" id="password" placeholder="mot de passe" required>
                                        <div class="d-flex">
                                            <button type="button" id="inscriptionBtn" class="form-control ms-2">Inscription</button>
                                        </div>
                                    </form>
                                    
                                </div>
                                <div class="tab-pane fade" id="password-tab-pane" role="tabpanel" aria-labelledby="password-tab" tabindex="0">
                                    <h6 class="mb-4">Connectez-Vous</h6>
                                    <form class="custom-form profile-form" id="loginForm" role="form">
                                        <div class="mb-3">
                                            <input class="form-control" type="email" id="login-email" placeholder="Johndoe@gmail.com" required>
                                        </div>
                                        <div class="mb-3">
                                            <input class="form-control" type="password" id="login-password" placeholder="Mot de passe" required>
                                        </div>
                                        <button type="submit" class="btn btn-primary w-100">Connexion</button>
                                    </form>
                                </div>

                               


                                
                                
            </div>
        </div>
    </div>

</center>
</div>
<footer class="site-footer">
    <div class="container">
        <div class="row">
            
            <div class="col-lg-12 col-12">
                <p class="copyright-text">ETU 2453 B-45 / ETU 2629 B-144/ ETU 2557 B-104 / ETU 2449 B-44 </p>
            </div>

        </div>
    </div>
</footer>

<div class="modal fade" id="pinModal" tabindex="-1" aria-labelledby="pinModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="pinModalLabel">Vérifiez votre code PIN</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label for="pin" class="form-label">Entrez le code PIN envoyé à votre email</label>
                    <input type="text" class="form-control" id="pin" placeholder="Code PIN" required>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fermer</button>
                <button type="button" class="btn btn-primary" id="submitPin">Valider le code PIN</button>
            </div>
        </div>
    </div>
</div>



</main>

</div>
</div>

<!-- JAVASCRIPT FILES -->
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/apexcharts.min.js"></script>
<script src="assets/js/custom.js"></script>

<script>
    document.getElementById("inscriptionBtn").addEventListener("click", function () {
        // Récupérer les données du formulaire
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        // Vérifier que les champs ne sont pas vides
        if (!email || !password) {
            alert("Veuillez remplir tous les champs.");
            return;
        }

        // Préparer les données pour l'API
        const data = {
            Email: email,
            Mdp: password
        };

        // Envoyer une requête POST à l'API
        fetch("http://localhost:5005/api/Inscription/sinscrire", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
        .then(response => response.json())
        .then(result => {
            if (result.status === "success") {
                alert(result.data.message);
            } else {
                alert(`Erreur : ${result.error}`);
            }
        })
        .catch(error => {
            console.error("Erreur lors de l'inscription :", error);
            alert("Une erreur s'est produite. Veuillez réessayer.");
        });
    });
</script>





<script>
    // Gestion du formulaire de connexion
    document.getElementById('loginForm').addEventListener('submit', async (event) => {
        event.preventDefault();

        const email = document.getElementById('login-email').value;
        const password = document.getElementById('login-password').value;

        const payload = { email, mdp: password };

        try {
            const response = await fetch('http://localhost:5005/api/Utilisateur/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload),
            });

            const data = await response.json();

            if (data.status === 'success') {
                alert('Connexion réussie. Code PIN envoyé.');
                const idUtilisateur = data.data.data.idUtilisateur;

                console.log(idUtilisateur);
                
                // Affichage du modal pour saisir le PIN
                $('#pinModal').modal('show');

                document.getElementById('submitPin').onclick = async () => {
                    const pin = document.getElementById('pin').value;

                    const pinPayload = { pin, idUtilisateur };
                    console.log(pinPayload);

                    try {
                        const pinResponse = await fetch('http://localhost:5005/api/Authentification/check-pin', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify(pinPayload),
                        });

                        const pinData = await pinResponse.json();

                        if (pinData.status === 'success') {
                            console.log('PINDATA OOOO')
                            
                            console.log(pinData.data.utilisateur.idUtilisateur);

                            alert('PIN vérifié avec succès.');

                           window.location.href = "http://localhost:8080/MyApp/login?user_id="+pinData.data.utilisateur.idUtilisateur; 
                            
                            
                            $('#pinModal').modal('hide');
                        } else {
                            alert(`Erreur : ${pinData.error}`);
                        }
                    } catch (error) {
                        console.error('Erreur réseau :', error);
                        alert('Une erreur est survenue lors de la validation du PIN.');
                    }
                };
            } else {
                alert(`Erreur : ${data.error}`);
            }
        } catch (error) {
            console.error('Erreur réseau :', error);
            alert('Une erreur est survenue. Veuillez réessayer.');
        }
    });
</script>


</body>
</html>