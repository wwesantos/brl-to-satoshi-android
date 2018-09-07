package co.javacafe.satoshirealconverter


private val EXCHANGE_RATE_URL = "https://api.coinmarketcap.com/v2/ticker/?convert=BRL&limit=1"

fun loadExhangeRateAPI(): String{
    try {
        val result =   khttp.get(EXCHANGE_RATE_URL)
                ?.jsonObject
                ?.getJSONObject("data")
                ?.getJSONObject("1")
                ?.getJSONObject("quotes")
                ?.getJSONObject("BRL")
                ?.getString("price")
        if(result != null) return result
        throw Exception("Error when loading the exchange rate.")
    }catch (e: Exception) {
        throw e
    }
}




