function calculateProjection() {
    const stockPrice = parseFloat(document.getElementById('stockPrice').value);
    let annualDividend = parseFloat(document.getElementById('annualDividend').value);
    const dividendCAGR = parseFloat(document.getElementById('dividendCAGR').value) / 100;
    const stockPriceCAGR = parseFloat(document.getElementById('stockPriceCAGR').value) / 100;
    const amountToInvest = parseFloat(document.getElementById('amountToInvest').value);
    const investmentFrequency = document.getElementById('investmentFrequency').value;
    const dividendFrequency = document.getElementById('dividendFrequency').value;
    const dividendReinvestment = document.getElementById('dividendReinvestment').value === 'yes';

    const years = 50;
    const frequencyMultiplier = {
        monthly: 12,
        quarterly: 4,
        semiAnnual: 2,
        annual: 1 }
        [dividendFrequency];

    //const results = [];

    //let totalShares = investmentFrequency === 'monthly' ? amountToInvest / stockPrice / 12 : amountToInvest / stockPrice;
    let totalShares = 0;
    let investedAmount = 0;
    let price = 0;
    let currentStockPrice = stockPrice;
    let annualDividendIncome = 0;

    console.clear()
    console.log("Year| "+"Invested| "+"Dividend")


    for (let year = 1; year <= years; year++) {
        // Update invested amount
        if (investmentFrequency === 'monthly') {
            investedAmount += amountToInvest * 12;
        } else if (investmentFrequency === 'oneTime' && year === 1) {
            investedAmount += amountToInvest;
        }


        totalShares = investedAmount / stockPrice;


        // Update the annual dividend after all intervals
        if (year === 1){
            annualDividend == annualDividend
        } else if (year>1) {
            annualDividend *= 1 + dividendCAGR;
        }



        // Store results every 5 years
        if (year % 5 === 0) {
            annualDividendIncome = totalShares*annualDividend
           console.log(year+"  |  "+investedAmount+"  |  "+annualDividendIncome.toFixed(2));
        }

    }
    console.log("Totalshares: "+totalShares);
    console.log("InvestedAmount: "+investedAmount);
    console.log("StockPrice: "+stockPrice+"\nStockPriceCAGR: "+stockPriceCAGR);
    console.log("Price: "+price)
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