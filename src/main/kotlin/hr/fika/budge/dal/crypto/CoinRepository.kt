package hr.fika.budge.dal.crypto

import hr.fika.budge.models.crypto.CoinTags
import hr.fika.budge.models.crypto.HistoricalCoinPrice

object CoinRepository {
    suspend fun getCoinPrice(tag: String) : Double? {
        val uuid = CoinTags.valueOf(tag).uuid
        val response = CoinApiProvider.provideCoinApi().getCoinPrice(uuid).body()
        response?.let {
            it.data?.let {
                return it.price!!.toDouble()
            }
        }
        return null
    }

    suspend fun getCoinPriceHistory(tag: String, period: String) : HistoricalCoinPrice? {
        val uuid = CoinTags.valueOf(tag).uuid
        val response = CoinApiProvider.provideCoinApi().getHistoricalPrice(uuid).body()
        response?.let {
            return it.toHistoricalCoinPrice()
        }
        return null
    }
}