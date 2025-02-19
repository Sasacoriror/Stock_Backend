async function sendData(){
    const stockTickerInn = document.getElementById("tickerSymbol").value.toUpperCase();
    let priceInn = document.getElementById("theStockPrice").value;
    let sharesInn = document.getElementById("shares").value;

    const response = await fetch("http://localhost:8080/api/v1/storeStockData", {
        method: "POST",
        headers: {"Content-Type": "application/json" },
        body: JSON.stringify({stockTickerInn, priceInn, sharesInn})
    });
    alert(await response.text());
}