package hr.fika.budge.dal.crypto

import hr.fika.budge.models.crypto.CoinTags
import hr.fika.budge.models.crypto.HistoricalCoinPrice

const val SEVEN_DAYS = "7d"
const val MONTH = "30d"
const val YEAR = "1y"

object CoinRepository {
    suspend fun getCoinPrice(tag: String): Double? {
        val uuid = CoinTags.valueOf(tag).uuid
        val response = CoinApiProvider.provideCoinApi().getCoinPrice(uuid).body()
        response?.let {
            it.data?.let {
                return it.price!!.toDouble()
            }
        }
        return null
    }

    suspend fun getCoinPriceHistory(tag: String, period: String): HistoricalCoinPrice? {
        val uuid = CoinTags.valueOf(tag).uuid
        val interval = when (period) {
            "week" -> SEVEN_DAYS
            "month" -> MONTH
            "year" -> YEAR
            else -> SEVEN_DAYS
        }
        val response = CoinApiProvider.provideCoinApi().getHistoricalPrice(uuid, period = interval).body()
        response?.let {
            return it.toHistoricalCoinPrice()
        }
        return null
    }
}