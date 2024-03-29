package hr.fika.budge.dal.stock

import hr.fika.budge.TWELVE_DATA_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object StockApiProvider {
    private var instance : StockApi? = null

    fun provideStockApi() : StockApi {
        if (instance == null) {
            instance = Retrofit.Builder().baseUrl(TWELVE_DATA_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(StockApi::class.java)
        }
        return instance!!
    }
}