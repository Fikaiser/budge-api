package hr.fika.budge.dal.stock

import hr.fika.budge.POLYGON_API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object StockApiProvider {
    private var instance : StockApi? = null

    fun provideStockApi() : StockApi {
        if (instance == null) {
            instance = Retrofit.Builder().baseUrl(POLYGON_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(StockApi::class.java)
        }
        return instance!!
    }
}