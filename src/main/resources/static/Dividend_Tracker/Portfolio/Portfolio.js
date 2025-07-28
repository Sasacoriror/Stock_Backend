const apiUrl = "http://localhost:8080/api/v1/findAll";

async function fetchStocks() {
    try {
        const response = await fetch(apiUrl);
        const data = await response.json();
        renderTable(data);
        renderSummary(data); // New summary rendering
    } catch (error) {
        console.error("Error fetching stock data:", error);
    }
}

function renderTable(stocks) {
    const tbody = document.querySelector('#stocksTable tbody');
    tbody.innerHTML = '';

    stocks.forEach((stock) => {
        const tr = document.createElement('tr');

        ['stockTickerInn', 'priceInn', 'sharesInn', 'currentPrice', 'dividend', 'totalDividend', 'totalPrice'].forEach(key => {
            const td = document.createElement('td');
            td.textContent = stock[key];
            tr.appendChild(td);
        });

        const actionTd = document.createElement('td');
        actionTd.innerHTML = `
            <button class="delete-btn" onclick="deleteRow('${stock.stockTickerInn}')">Delete</button>
        `;
        tr.dataset.id = stock.id;
        tr.appendChild(actionTd);
        tbody.appendChild(tr);
    });
}

function renderSummary(stocks) {
    let totalValue = 0;
    let totalCost = 0;
    let totalDividends = 0;

    stocks.forEach(stock => {
        const shares = parseFloat(stock.sharesInn);
        const buyPrice = parseFloat(stock.priceInn);
        const currentPrice = parseFloat(stock.currentPrice);
        const totalDividend = parseFloat(stock.totalDividend);

        totalValue += currentPrice * shares;
        totalCost += buyPrice * shares;
        totalDividends += totalDividend;
    });

    const profit = totalValue - totalCost;

    document.getElementById("totalInvested").textContent = `$${totalCost.toFixed(2)}`
    document.getElementById("totalValue").textContent = `$${totalValue.toFixed(2)}`;
    document.getElementById("totalProfit").textContent = `$${profit.toFixed(2)}`;
    document.getElementById("totalDividends").textContent = `$${totalDividends.toFixed(2)}`;
}

function deleteRow(id) {
    fetch(`http://localhost:8080/api/v1/delete/${id}`, {
        method: 'DELETE'
    })
        .then(() => fetchStocks())
        .catch(error => console.error("Delete failed:", error));
}

// Load on page load
fetchStocks();
