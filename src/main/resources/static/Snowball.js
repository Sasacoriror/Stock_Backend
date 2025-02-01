function calculateProjection() {
    let stockPrice = parseFloat(document.getElementById('stockPrice').value);
    let annualDividend = parseFloat(document.getElementById('annualDividend').value);
    const dividendCAGR = parseFloat(document.getElementById('dividendCAGR').value) / 100;
    const stockPriceCAGR = parseFloat(document.getElementById('stockPriceCAGR').value) / 100;
    const amountToInvest = parseFloat(document.getElementById('amountToInvest').value);
    const investmentFrequency = document.getElementById('investmentFrequency').value;
    const dividendFrequency = document.getElementById('dividendFrequency').value.toLowerCase();
    const dividendReinvestment = document.getElementById('dividendReinvestment').value === 'yes';

    let periodsPerYear;
    if (dividendFrequency === 'monthly') {
        periodsPerYear = 12;
    } else if (dividendFrequency === 'quarterly') {
        periodsPerYear = 4;
    } else if (dividendFrequency === 'semiannual' || dividendFrequency === 'semiAnnual') {
        periodsPerYear = 2;
    } else if (dividendFrequency === 'annual') {
        periodsPerYear = 1;
    } else {
        console.error("Unknown dividend frequency: " + dividendFrequency);
        return;
    }

    const totalYears = 50;
    const totalPeriods = totalYears * periodsPerYear;

    // Convert annual growth rates into per-period growth factors.
    const periodStockPriceGrowth = Math.pow(1 + stockPriceCAGR, 1 / periodsPerYear);
    // Option 1: Update dividend only once per year (keeps dividend constant during the year).
    // Option 2: Convert dividend growth to per-period if you want it to change every period.
    // For this example, we update dividends annually.

    let totalShares = 0;
    if (investmentFrequency === 'oneTime') {
        totalShares = amountToInvest / stockPrice;
        console.log("Initial Shares:", totalShares);
    }
    // (For monthly investing, you would add investments each period.)

    console.clear();
    console.log("Year | Annual Dividend Income");

    for (let period = 1; period <= totalPeriods; period++) {
        // For monthly investment, add new shares each period:
        if (investmentFrequency === 'monthly') {
            // Here, amountToInvest is the amount invested each month.
            //totalShares += amountToInvest / stockPrice;
            //console.log("Initial Shares:", totalShares);

        }

        // Update stock price for each period.
        stockPrice *= periodStockPriceGrowth;

        // Calculate dividend payment for this period (if dividends are paid monthly, use annualDividend / 12)
        if (dividendReinvestment) {
            const dividendPayment = (annualDividend / periodsPerYear) * totalShares;
            // Reinvest at the current stock price:
            const newShares = dividendPayment / stockPrice;
            console.log("Dividend Payment:", dividendPayment.toFixed(4), "New Shares:", newShares.toFixed(6));
            totalShares += newShares;
        }

        // At the end of each year, update the annual dividend and log the annual dividend income.
        if (period % periodsPerYear === 0) {
            let currentYear = period / periodsPerYear;
            let annualDividendIncome = totalShares * annualDividend;
            console.log(currentYear + "   |  " + annualDividendIncome.toFixed(2));

            // Update annual dividend for the next year.
            annualDividend *= 1 + dividendCAGR;
        }
    }

    console.log("Total Shares: " + totalShares.toFixed(4));
    console.log("Final Stock Price: " + stockPrice.toFixed(2));
    //generateTable(results);
}





function generateTable(data) {
    const tableContainer = document.getElementById('tableContainer');
    tableContainer.innerHTML = ''; // Clear previous table

    const table = document.createElement('table');
    const thead = document.createElement('thead');
    const tbody = document.createElement('tbody');

    // Add header row
    thead.innerHTML = `
    <tr>
      <th>Year</th>
      <th>Invested Amount</th>
      <th>Annual Dividend Income</th>
      <th>Total Dividends Earned</th>
      <th>Value of Stock</th>
    </tr>
  `;

    // Add rows for each year
    data.forEach(row => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
      <td>${row.year}</td>
      <td>${row.investedAmount}</td>
      <td>${row.annualDividendIncome}</td>
      <td>${row.totalDividendsEarned}</td>
      <td>${row.stockValue}</td>
    `;
        tbody.appendChild(tr);
    });

    table.appendChild(thead);
    table.appendChild(tbody);
    tableContainer.appendChild(table);
}