package hr.fika.budge.dal.stock

import hr.fika.budge.models.stock.HistoricalStockPrice
import hr.fika.budge.utils.CurrencyUtils

object StockRepository {
    suspend fun getStockPrice(tag: String) : Double? {
        val response = StockApiProvider.provideStockApi().getStockPrice(tag).body()
        response?.let {
            it.price?.let { price ->
                return CurrencyUtils.convertUsdToEur(price.toDouble())
            }
        }
        return null
    }

    suspend fun getStockPriceHistory(tag: String, interval: String, outputSize: Int) : HistoricalStockPrice? {
        val response = StockApiProvider.provideStockApi().getHistoricalPrices(tag, outputSize, interval).body()
        response?.let {
            return it.toHistoricalStockPrice()
        }
        return null
    }
}
