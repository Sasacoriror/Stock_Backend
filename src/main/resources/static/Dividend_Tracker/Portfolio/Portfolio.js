const apiUrl = "http://localhost:8080/api/v1/findAll";

async function fetchStocks() {
    try {
        const response = await fetch(apiUrl);
        const data = await response.json();
        renderTable(data);
    } catch (error) {
        console.error("Error fetching stock data:", error);
    }
}

function renderTable(stocks) {
    const tbody = document.querySelector('#stocksTable tbody');
    tbody.innerHTML = ''; // Clear old rows

    stocks.forEach((stock) => {
        const tr = document.createElement('tr');

        ['stockTickerInn', 'priceInn', 'sharesInn', 'currentPrice', 'dividend', 'totalDividend', 'totalPrice'].forEach(key => {
            const td = document.createElement('td');
            td.textContent = stock[key];
            tr.appendChild(td);
        });

        // Actions column
        const actionTd = document.createElement('td');
        actionTd.innerHTML = `
                <button class="edit-btn">Edit</button>
                <button class="delete-btn" onclick="deleteRow(${stock.id})">Delete</button>
            `;
        tr.dataset.id = stock.id; // Store ID in row
        tbody.appendChild(tr);
        tr.appendChild(actionTd);
    });
}

function deleteRow(id) {
    // DELETE request to backend (uncomment to enable)

    fetch(`http://localhost:8080/api/v1/delete/${id}`, {
        method: 'DELETE'
    }).then(() => fetchStocks());
    console.log("Deleted ID:", id);
    fetchStocks(); // Refresh table (mock behavior)
}

// Load data on page load
fetchStocks();