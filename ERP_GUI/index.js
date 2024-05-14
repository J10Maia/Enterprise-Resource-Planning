document.addEventListener('DOMContentLoaded', function() {
    // Fetch orders data
    fetch('http://localhost:3000/api/orders')
        .then(response => response.json())
        .then(data => {
            console.log('Fetched orders data:', data);
            const tableBody = document.querySelector('#ordersTable tbody');
            data.forEach(row => {
                const tr = document.createElement('tr');

                const numberTd = document.createElement('td');
                numberTd.textContent = row.number;
                tr.appendChild(numberTd);

                const workpieceTd = document.createElement('td');
                workpieceTd.textContent = row.workpiece;
                tr.appendChild(workpieceTd);

                const quantityTd = document.createElement('td');
                quantityTd.textContent = row.quantity;
                tr.appendChild(quantityTd);

                const dueDateTd = document.createElement('td');
                dueDateTd.textContent = row.duedate;
                tr.appendChild(dueDateTd);

                const latePenaltyTd = document.createElement('td');
                latePenaltyTd.textContent = row.latepenalty;
                tr.appendChild(latePenaltyTd);

                const earlyPenaltyTd = document.createElement('td');
                earlyPenaltyTd.textContent = row.earlypenalty;
                tr.appendChild(earlyPenaltyTd);

                const clientNameIdTd = document.createElement('td');
                clientNameIdTd.textContent = row.clientnameid;
                tr.appendChild(clientNameIdTd);

                tableBody.appendChild(tr);
            });
        })
        .catch(error => console.error('Error fetching orders:', error));

    // Fetch planning data
    fetch('http://localhost:3000/api/planningtable')
        .then(response => response.json())
        .then(data => {
            console.log('Fetched planning data:', data);
            const tableBody = document.querySelector('#planningTable tbody');
            data.forEach(row => {
                const tr = document.createElement('tr');

                const idTd = document.createElement('td');
                idTd.textContent = row.id;
                tr.appendChild(idTd);

                const dayTd = document.createElement('td');
                dayTd.textContent = row.day;
                tr.appendChild(dayTd);

                const pecaInicialTd = document.createElement('td');
                // Replace "InitialPiece" with an empty string
                pecaInicialTd.textContent = row.peca_inicial === 'InitialPiece' ? 'No piece needed' : row.peca_inicial;
                tr.appendChild(pecaInicialTd);

                const pecaFinalTd = document.createElement('td');
                pecaFinalTd.textContent = row.peca_final;
                tr.appendChild(pecaFinalTd);

                tableBody.appendChild(tr);
            });
        })
        .catch(error => console.error('Error fetching planning data:', error));
});
