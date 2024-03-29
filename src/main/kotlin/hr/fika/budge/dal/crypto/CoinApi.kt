package hr.fika.budge.dal.crypto

import hr.fika.budge.COINRANKING_API_HOST
import hr.fika.budge.RAPIDAPI_API_KEY
import hr.fika.budge.EUR_ID
import hr.fika.budge.models.crypto.CoinHistoryResponse
import hr.fika.budge.models.crypto.CoinPriceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface CoinApi {
    @Headers("X-RapidAPI-Key: $RAPIDAPI_API_KEY", "X-RapidAPI-Host: $COINRANKING_API_HOST")
    @GET("/coin/{tag}/price")
    suspend fun getCoinPrice(
        @Path("tag") tag: String,
        @Query("referenceCurrencyUuid") currency: String = EUR_ID
    ): Response<CoinPriceResponse>

    @Headers("X-RapidAPI-Key: $RAPIDAPI_API_KEY", "X-RapidAPI-Host: $COINRANKING_API_HOST")
    @GET("/coin/{tag}/history")
    suspend fun getHistoricalPrice(
        @Path("tag") tag: String,
        @Query("timePeriod") period: String,
        @Query("referenceCurrencyUuid") currency: String = EUR_ID
    ): Response<CoinHistoryResponse>
}
