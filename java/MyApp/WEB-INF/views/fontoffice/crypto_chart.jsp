<%@ include file="/header.jsp" %>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crypto Line Chart</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script> <!-- Chart.js CDN -->
    <style>
        canvas {
            max-width: 100%;
            height: auto;
        }
    </style>
</head>
<body>
    <h1>Crypto Line Chart</h1>
    <canvas id="cryptoLineChart"></canvas> <!-- Canevas pour le graphique -->

   <script>
    const chartInstance = new Chart(document.getElementById('cryptoLineChart'), {
        type: 'line',
        data: {
            labels: [], // Sera mis à jour dynamiquement
            datasets: [] // Chaque dataset représente une cryptomonnaie
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: true }
            },
            scales: {
                x: { title: { display: true, text: 'Dates' } },
                y: { title: { display: true, text: 'Valeurs' }, beginAtZero: true }
            }
        }
    });

    // Palette de couleurs pour les différentes cryptomonnaies
    const colors = [
    'rgba(255, 99, 132, 1)', 'rgba(54, 162, 235, 1)',
    'rgba(75, 192, 192, 1)', 'rgba(153, 102, 255, 1)',
    'rgba(255, 159, 64, 1)', 'rgba(255, 206, 86, 1)',
    'rgba(231, 233, 237, 1)', 'rgba(32, 158, 109, 1)',
    'rgba(242, 85, 96, 1)', 'rgba(120, 120, 255, 1)'
    ];


    // Récupérer et afficher les données
    function loadCryptoData() {
        fetch('crypto-chart')
            .then(response => response.json())
            .then(data => {
                // Mettre à jour les labels et les datasets
                const newLabels = data[0]?.map(point => point.date) || [];
                const newDatasets = data.map((coursList, index) => ({
                    label: `Cryptomonnaie ${index + 1}`,
                    data: coursList.map(point => point.valeur),
                    borderColor: colors[index % colors.length],
                    backgroundColor: colors[index % colors.length].replace('1)', '0.2)'),
                    borderWidth: 2,
                    tension: 0.4
                }));

                chartInstance.data.labels = newLabels;
                chartInstance.data.datasets = newDatasets;
                chartInstance.update();
            })
            .catch(error => console.error('Erreur:', error));
    }

    // Charger les données toutes les 10 secondes
    loadCryptoData();
    setInterval(loadCryptoData, 10000);
</script>

<%@ include file="/footer.jsp" %>