package hr.fika.budge.dal.currency

import hr.fika.budge.RAPIDAPI_API_KEY
import hr.fika.budge.TWELVE_DATA_API_HOST
import hr.fika.budge.models.currency.ExchangeRateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CurrencyApi {
    /**
     * Method for fetching the exchange rate of two currencies
     *
     * @param currency Parameter designating the two currencies for which we are fetching the rate
     * passed in the form CUR1/CUR2 ex. USD/EUR
     */
    @Headers("X-RapidAPI-Key: $RAPIDAPI_API_KEY", "X-RapidAPI-Host: $TWELVE_DATA_API_HOST")
    @GET("/exchange_rate")
    suspend fun getExchangeRate(
        @Query("symbol") currency: String
    ): Response<ExchangeRateResponse>
}