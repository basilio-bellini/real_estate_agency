function loadChartData() {
    fetch('/api/AppartmentData')
        .then(response => response.json())
        .then(data => {
            const labels = data.map(item => item.category);
            const counts = data.map(item => item.average_price);

            const ctx = document.getElementById('appartmentChart').getContext('2d');
            const appartmentChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Средняя цена квартир',
                        data: counts,
                        backgroundColor: 'rgba(75, 192, 192, 0.5)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: 'Средняя цена квартир'
                            }
                        },
                        x: {
                            title: {
                                display: true,
                                text: 'Категория'
                            }
                        }
                    }
                }
            });
        })
        .catch(error => console.error('Ошибка при загрузке данных:', error));
}

window.onload = loadChartData;