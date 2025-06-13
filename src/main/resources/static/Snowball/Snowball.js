function calculateProjection() {
    let stockPrice = parseFloat(document.getElementById('stockPrice').value);
    let annualDividend = parseFloat(document.getElementById('annualDividend').value);
    const dividendCAGR = parseFloat(document.getElementById('dividendCAGR').value) / 100;
    const stockPriceCAGR = parseFloat(document.getElementById('stockPriceCAGR').value) / 100;
    const amountToInvest = parseFloat(document.getElementById('amountToInvest').value);
    const investmentFrequency = document.getElementById('investmentFrequency').value;
    const dividendFrequency = document.getElementById('dividendFrequency').value.toLowerCase();
    const dividendReinvestment = document.getElementById('dividendReinvestment').value === 'yes';

    calculateProjection2()

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

    const periodStockPriceGrowth = Math.pow(1 + stockPriceCAGR, 1 / periodsPerYear);

    let totalShares = 0;
    if (investmentFrequency === 'oneTime') {
        totalShares = amountToInvest / stockPrice;
    }

    let results = [];

    for (let period = 1; period <= totalPeriods; period++) {
        stockPrice *= periodStockPriceGrowth;

        if (dividendReinvestment) {
            const dividendPayment = (annualDividend / periodsPerYear) * totalShares;
            const newShares = dividendPayment / stockPrice;
            totalShares += newShares;
        }

        if (period % periodsPerYear === 0) {
            let currentYear = period / periodsPerYear;
            if (currentYear % 5 === 0) {  // Only store data every 5 years
                let annualDividendIncome = totalShares * annualDividend;
                results.push({
                    year: currentYear,
                    investedAmount: amountToInvest.toFixed(2),
                    annualDividendIncome: annualDividendIncome.toFixed(2)
                });
            }
            annualDividend *= 1 + dividendCAGR;
        }
    }

    generateTable(results);  // Generate the table with the results

    console.log("Total Shares: " + totalShares.toFixed(4));
    console.log("Final Stock Price: " + stockPrice.toFixed(2));
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
    </tr>
  `;

    // Add rows for each year
    data.forEach(row => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
      <td>${row.year}</td>
      <td>${row.investedAmount}</td>
      <td>${row.annualDividendIncome}</td>
    `;
        tbody.appendChild(tr);
    });

    table.appendChild(thead);
    table.appendChild(tbody);
    tableContainer.appendChild(table);
}