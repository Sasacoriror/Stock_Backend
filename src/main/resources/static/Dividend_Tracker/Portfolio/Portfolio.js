const apiUrl = "http://localhost:8080/api/v1/findAll";

async function fetchStocks() {
    try {
        const response = await fetch(apiUrl);
        const data = await response.json();
        renderTable(data);
        renderSummary(data);
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
            <button class="edit-btn" onclick="openEditRow('${stock.stockTickerInn}', '${stock.sharesInn}', '${stock.priceInn}')">Edit</button>
        `;
        tr.dataset.id = stock.id;
        tr.appendChild(actionTd);
        tbody.appendChild(tr);
    });
}


// TODO: Add percentage of unrealised gain/loss, then color refplecting if it is positive or negative
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
    const percentage = ((profit) / totalCost) * 100;

    const positiveOrNegative = `${profit >= 0 ? '+' : ''}$${profit.toFixed(2)}, ${percentage >= 0 ? '+' : ''}${percentage.toFixed(2)}%`;


    document.getElementById("totalInvested").textContent = `$${totalCost.toFixed(2)}`
    document.getElementById("totalValue").textContent = `$${totalValue.toFixed(2)}`;
    document.getElementById("totalDividends").textContent = `$${totalDividends.toFixed(2)}`;

    const tProfit = document.getElementById("totalProfit");

    tProfit.textContent = positiveOrNegative;
    tProfit.style.color = profit >= 0 ? "green" : "red";


}

let stockName= "";

function openEditRow(id, shares, price){

    stockName = id;
    document.getElementById("editShares").value = shares;
    document.getElementById("editPrice").value = price;
    document.getElementById("editModal").style.display = "block";
    console.log("ticker: "+id+" | Shares: "+shares+" | Price: "+price);
}

async function editRow(){
    let sharesInn = document.getElementById("editShares").value;
    let priceInn = document.getElementById("editPrice").value;

    const response = await fetch(`http://localhost:8080/api/v1/updateData/${stockName}`, {
        method: "PUT",
        headers: {"Content-Type": "application/json" },
        body: JSON.stringify({sharesInn, priceInn})
    });
    await response.text();
    console.log("Sending to backend to update data.")
    closeEditModal();
    fetchStocks();
}

window.onclick = function(event) {
    const modal = document.getElementById("editModal");
    if (event.target === modal) {
        closeEditModal();
    }
};

function closeEditModal() {
    document.getElementById("editModal").style.display = "none";
}

function deleteRow(id) {
    fetch(`http://localhost:8080/api/v1/delete/${id}`, {
        method: 'DELETE'
    })
        .then(() => fetchStocks())
        .catch(error => console.error("Delete failed:", error));
}

//Load page
fetchStocks();
