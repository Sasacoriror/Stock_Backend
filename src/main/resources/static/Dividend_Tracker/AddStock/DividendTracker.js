async function sendData(){
    const stockTickerInn = document.getElementById("tickerSymbol").value.toUpperCase();
    let priceInn = document.getElementById("theStockPrice").value;
    let sharesInn = document.getElementById("shares").value;

    try {
        const response = await fetch("http://localhost:8080/api/v1/storeStockData", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                stockTickerInn,
                priceInn,
                sharesInn
            })
        });

        if (!response.ok){
            throw new Error(`Error: ${response.status} ${response.statusText}`)
        }

        await response.text();
        console.log("Stock successfully added")
        emptyField();
    }catch (error){
        alert(`Failed to send data: ${error.message}`)
    }


}

function emptyField(){
    document.getElementById("tickerSymbol").value = "";
    document.getElementById("theStockPrice").value = "";
    document.getElementById("shares").value = "";
}