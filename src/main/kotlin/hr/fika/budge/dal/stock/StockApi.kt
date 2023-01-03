package hr.fika.budge.dal.stock

import hr.fika.budge.COINRANKING_API_HOST
import hr.fika.budge.COINRANKING_API_KEY
import hr.fika.budge.models.crypto.CoinPriceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface StockApi {
    @Headers("X-RapidAPI-Key: $COINRANKING_API_KEY", "X-RapidAPI-Host: $COINRANKING_API_HOST")
    @GET("/coin/Qwsogvtv82FCd/price")
    suspend fun getCoinPrice() : Response<CoinPriceResponse>
}