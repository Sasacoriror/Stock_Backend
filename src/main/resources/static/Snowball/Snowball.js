fetch("../Index/index.html")
            .then(res => res.text())
            .then(data => {
                document.getElementById("navbar").innerHTML = data;
                const link = document.createElement("link");
                link.rel = "stylesheet";
                link.href = "../Index/navbar.css";
                document.head.appendChild(link);

                initThemeToggle();
            });

function run(){
    genTableData();
    genChartData();
}

function getData(){
    let stockPrice = parseFloat(document.getElementById("stockPrice").value);
    let annualDividend = parseFloat(document.getElementById("annualDividend").value);
    const dividendCAGR = parseFloat(document.getElementById("dividendCAGR").value) / 100;
    const stockPriceCAGR = parseFloat(document.getElementById("stockPriceCAGR").value) / 100;
    const amountToInvest = parseFloat(document.getElementById("amountToInvest").value);
    const investmentFrequency = document.getElementById("investmentFrequency").value;
    const dividendReinvestment = document.getElementById("dividendReinvestment").value === "yes";

    if(isNaN(stockPrice) || isNaN(annualDividend) || isNaN(dividendCAGR) || isNaN(stockPriceCAGR) || isNaN(amountToInvest)){
        alert("All boxes must be filled inn!");
        return null;
    }

    return {stockPrice, annualDividend, dividendCAGR, stockPriceCAGR, amountToInvest, investmentFrequency, dividendReinvestment}
}

function genTableData(){
    let data = getData();
    if(!data){
        return;
    }

    let {stockPrice, annualDividend, dividendCAGR, stockPriceCAGR,
     amountToInvest, investmentFrequency, dividendReinvestment} = data;

    const totalYears = 50;
    let moneyPutInn = amountToInvest;
    const stockPriceGrowth = Math.pow(1 + stockPriceCAGR, 1 / 1);

    let totalShares = 0;
    if (investmentFrequency === "oneTime") {
        totalShares = amountToInvest / stockPrice;
    }

    let results = [];
    for(let period = 1; period <= totalYears; period++){

        if(investmentFrequency === "monthly") {
            totalShares += (amountToInvest * 12) / stockPrice;
            moneyPutInn += amountToInvest * 12;
        }


        stockPrice *= stockPriceGrowth;

        if(dividendReinvestment){
            const dividendPayment = annualDividend * totalShares;
            const dripShares = dividendPayment / stockPrice;
            totalShares += dripShares;
        }



        if(period % 1 === 0){
            let currentYear = period / 1;
            if(currentYear % 5 === 0){
                let annualDividendIncome = totalShares * annualDividend;
                console.log(currentYear+" invested: "+moneyPutInn);
                results.push({
                    year: currentYear,
                    investedAmount: moneyPutInn.toFixed(2),
                    annualDividendIncome: annualDividendIncome.toFixed(2)
                });
            }
            annualDividend *= 1 + dividendCAGR;
        }
    }
    createTable(results);
}

function createTable(data){
    const tableContainer = document.getElementById("tableContainer");
    tableContainer.innerHTML = "";

    const table = document.createElement("table");
    const thead = document.createElement("thead");
    const tbody = document.createElement("tbody");

   thead.innerHTML = `
       <tr>
           <th>Year</th>
           <th>Invested Amount</th>
           <th>Annual Dividend Income</th>
       </tr>`;

   data.forEach(row => {
       const tr = document.createElement("tr");
       tr.innerHTML = `
           <td>${row.year}</td>
           <td>${row.investedAmount}</td>
           <td>${row.annualDividendIncome}</td>`;
       tbody.appendChild(tr);
   });

   table.appendChild(thead);
   table.appendChild(tbody);
   tableContainer.appendChild(table);

}

function genChartData(){
}

function genChart(){
}