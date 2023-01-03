package hr.fika.budge.dal.currency

import hr.fika.budge.TWELVE_DATA_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CurrencyApiProvider {
    private var instance : CurrencyApi? = null

    fun provideCurrencyApi() : CurrencyApi {
        if (instance == null) {
            instance = Retrofit.Builder().baseUrl(TWELVE_DATA_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CurrencyApi::class.java)
        }
        return instance!!
    }
}