document.addEventListener('DOMContentLoaded', function() {
    // Fetch and display the current day
    displayCurrentDay();

    // Fetch refused orders
    displayRefusedOrders();

    // Fetch orders data
    fetchOrdersData();

    // Fetch planning data
    fetchPlanningData();
});

function displayCurrentDay() {
    fetch('http://localhost:3000/api/current_minute')
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                const dayInt = data[0].minute; // Assuming 'minute' holds the day integer
                document.getElementById('currentDay').textContent = `Day: ${dayInt}`;
            } else {
                document.getElementById('currentDay').textContent = 'Day: Not available';
            }
        })
        .catch(error => {
            console.error('Error fetching current day:', error);
            document.getElementById('currentDay').textContent = 'Day: Error fetching day';
        });
}

function displayRefusedOrders() {
    fetch('http://localhost:3000/api/refused_order')
        .then(response => response.json())
        .then(data => {
            const refusedOrders = data.map(order => order.order_number).join(', ') || 'None';
            document.getElementById('refusedOrders').textContent = `Refused Orders: ${refusedOrders}`;
        })
        .catch(error => {
            console.error('Error fetching refused orders:', error);
            document.getElementById('refusedOrders').textContent = 'Refused Orders: Error fetching orders';
        });
}

function fetchOrdersData() {
    fetch('http://localhost:3000/api/orders')
        .then(response => response.json())
        .then(ordersData => {
            console.log('Fetched orders data:', ordersData);
            populateOrdersTable(ordersData);
            fetchDurationData(ordersData);
        })
        .catch(error => console.error('Error fetching orders:', error));
}

function populateOrdersTable(ordersData) {
    const tableBody = document.querySelector('#ordersTable tbody');
    if (!tableBody) {
        console.error('Orders table body not found');
        return;
    }

    ordersData.forEach(row => {
        const tr = document.createElement('tr');

        tr.appendChild(createTableCell(row.number, 'index'));
        tr.appendChild(createTableCell(row.workpiece));
        tr.appendChild(createTableCell(row.quantity));
        tr.appendChild(createTableCell(row.duedate));
        tr.appendChild(createTableCell(row.latepenalty));
        tr.appendChild(createTableCell(row.earlypenalty));
        tr.appendChild(createTableCell(row.clientnameid));

        tableBody.appendChild(tr);
    });
}

function fetchDurationData(ordersData) {
    fetch('http://localhost:3000/api/duration')
        .then(response => response.json())
        .then(durationData => {
            console.log('Fetched duration data:', durationData);
            const durationMap = createDurationMap(durationData);
            calculateAndPopulateOrderCost(ordersData, durationMap);
        })
        .catch(error => console.error('Error fetching duration data:', error));
}

function createDurationMap(durationData) {
    const durationMap = {};
    durationData.forEach(row => {
        durationMap[row.order_number] = row;
    });
    return durationMap;
}

function calculateAndPopulateOrderCost(ordersData, durationMap) {
    const orderCostTableBody = document.querySelector('#orderCostTable tbody');
    if (!orderCostTableBody) {
        console.error('Order cost table body not found');
        return;
    }

    ordersData.forEach(row => {
        const orderNumber = row.number;
        const quantity = parseInt(row.quantity);
        const duration = durationMap[orderNumber];

        if (duration) {
            const Pt = calculatePt(duration.begin_date, duration.finish_date);
            const Rc = calculateRc(quantity);
            const Pc = calculatePc(Pt, Rc);

            const newRow = orderCostTableBody.insertRow();
            const cell1 = newRow.insertCell(0);
            const cell2 = newRow.insertCell(1);

            cell1.innerText = orderNumber;
            cell2.innerText = `$${Pc.toFixed(2)}`;
            console.log(`Added row for order ${orderNumber} with Pc = $${Pc.toFixed(2)}`);
        } else {
            console.error(`No duration data found for order number ${orderNumber}`);
        }
    });
}

function calculatePt(beginDate, finishDate) {
    return (parseInt(finishDate) - parseInt(beginDate)) * 60;
}

function calculateRc(quantity) {
    return Math.ceil(quantity * 55 / 4);
}

function calculatePc(Pt, Rc) {
    // Implement the actual calculation logic for Pc here
    return (Pt * Rc) / 100;
}

function fetchPlanningData() {
    fetch('http://localhost:3000/api/planningtable')
        .then(response => response.json())
        .then(data => {
            console.log('Fetched planning data:', data);
            populatePlanningTable(data);
        })
        .catch(error => console.error('Error fetching planning data:', error));
}

function populatePlanningTable(data) {
    const tableBody = document.querySelector('#planningTable tbody');
    if (!tableBody) {
        console.error('Planning table body not found');
        return;
    }

    data.forEach(row => {
        const tr = document.createElement('tr');

        tr.appendChild(createTableCell(row.id, 'index'));
        tr.appendChild(createTableCell(row.day));
        tr.appendChild(createTableCell(row.peca_inicial === 'InitialPiece' ? 'No piece needed' : row.peca_inicial));
        tr.appendChild(createTableCell(row.peca_final));

        tableBody.appendChild(tr);
    });
}

function createTableCell(content, className = '') {
    const td = document.createElement('td');
    td.textContent = content;
    if (className) {
        td.classList.add(className);
    }
    return td;
}