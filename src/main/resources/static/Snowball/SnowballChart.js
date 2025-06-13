function calculateProjection2() {
    let stockPrice = parseFloat(document.getElementById('stockPrice').value);
    let annualDividend = parseFloat(document.getElementById('annualDividend').value);
    const dividendCAGR = parseFloat(document.getElementById('dividendCAGR').value) / 100;
    const stockPriceCAGR = parseFloat(document.getElementById('stockPriceCAGR').value) / 100;
    const amountToInvest = parseFloat(document.getElementById('amountToInvest').value);
    const investmentFrequency = document.getElementById('investmentFrequency').value;
    const dividendFrequency = document.getElementById('dividendFrequency').value.toLowerCase();

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

    let totalSharesReinvest = amountToInvest / stockPrice;  // With reinvestment
    let totalSharesNoReinvest = amountToInvest / stockPrice;  // Without reinvestment

    let results = [];
    let years = [];
    let dividendsWithReinvest = [];
    let dividendsWithoutReinvest = [];

    for (let period = 1; period <= totalPeriods; period++) {
        stockPrice *= periodStockPriceGrowth;

        // With reinvestment
        const dividendPaymentReinvest = (annualDividend / periodsPerYear) * totalSharesReinvest;
        const newSharesReinvest = dividendPaymentReinvest / stockPrice;
        totalSharesReinvest += newSharesReinvest;

        // Without reinvestment (no new shares are bought)
        const dividendPaymentNoReinvest = (annualDividend / periodsPerYear) * totalSharesNoReinvest;

        if (period % periodsPerYear === 0) {
            let currentYear = period / periodsPerYear;
            if (currentYear % 5 === 0) {  // Only store data every 5 years
                let annualDividendIncomeReinvest = totalSharesReinvest * annualDividend;
                let annualDividendIncomeNoReinvest = totalSharesNoReinvest * annualDividend;

                results.push({
                    year: currentYear,
                    investedAmount: amountToInvest.toFixed(2),
                    annualDividendIncomeReinvest: annualDividendIncomeReinvest.toFixed(2),
                    annualDividendIncomeNoReinvest: annualDividendIncomeNoReinvest.toFixed(2)
                });

                // Store for chart
                years.push(currentYear);
                dividendsWithReinvest.push(annualDividendIncomeReinvest);
                dividendsWithoutReinvest.push(annualDividendIncomeNoReinvest);
            }
            annualDividend *= 1 + dividendCAGR;
        }
    }

    generateChart(years, dividendsWithReinvest, dividendsWithoutReinvest);
}


// Function to generate the line chart
function generateChart(years, reinvestedDividends, noReinvestedDividends) {
    const ctx = document.getElementById('dividendChart').getContext('2d');

    // Destroy previous chart instance if it exists
    if (window.dividendChartInstance) {
        window.dividendChartInstance.destroy();
    }

    window.dividendChartInstance = new Chart(ctx, {
        type: 'line',
        data: {
            labels: years,
            datasets: [
                {
                    label: 'Annual Dividend (With Reinvestment)',
                    data: reinvestedDividends,
                    borderColor: 'blue',
                    backgroundColor: 'rgba(0, 0, 255, 0.2)',
                    fill: true
                },
                {
                    label: 'Annual Dividend (Without Reinvestment)',
                    data: noReinvestedDividends,
                    borderColor: 'red',
                    backgroundColor: 'rgba(255, 0, 0, 0.2)',
                    fill: true
                }
            ]
        },
        options: {
            responsive: true,
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'Year'
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: 'Annual Dividend Income ($)'
                    },
                    beginAtZero: true
                }
            }
        }
    });
}
