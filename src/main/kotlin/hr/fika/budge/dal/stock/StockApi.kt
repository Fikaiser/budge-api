package hr.fika.budge.dal.stock

import hr.fika.budge.RAPIDAPI_API_KEY
import hr.fika.budge.TWELVE_DATA_API_HOST
import hr.fika.budge.models.stock.HistoricalStockPriceResponse
import hr.fika.budge.models.stock.StockPriceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface StockApi {
    @Headers("X-RapidAPI-Key: $RAPIDAPI_API_KEY", "X-RapidAPI-Host: $TWELVE_DATA_API_HOST")
    @GET("/price")
    suspend fun getStockPrice(
        @Query("symbol") tag: String
    ): Response<StockPriceResponse>

    @Headers("X-RapidAPI-Key: $RAPIDAPI_API_KEY", "X-RapidAPI-Host: $TWELVE_DATA_API_HOST")
    @GET("/time_series")
    suspend fun getHistoricalPrices(
        @Query("symbol") tag: String,
        @Query("outputsize") outputSize: Int,
        @Query("interval") interval: String,
    ): Response<HistoricalStockPriceResponse>
}