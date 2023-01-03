package hr.fika.budge.dal.crypto

import hr.fika.budge.COINRANKING_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CoinApiProvider {
    private var instance : CoinApi? = null

    fun provideCoinApi() : CoinApi {
        if (instance == null) {
            instance = Retrofit.Builder().baseUrl(COINRANKING_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CoinApi::class.java)
        }
        return instance!!
    }
}