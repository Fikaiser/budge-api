package hr.fika.budge.utils

import hr.fika.budge.dal.currency.CurrencyApiProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.math.RoundingMode

object CurrencyUtils {
    private var exchangeRate: Double? = null

    init {
        CoroutineScope(Job()).launch {
            getExchangeRate()
        }
    }

    fun convertUsdToEur(usdAmount: Double): Double {
        val conversionRate = exchangeRate ?: 0.95
        return (usdAmount * conversionRate).toBigDecimal().setScale(2, RoundingMode.DOWN).toDouble()
    }

    private suspend fun getExchangeRate() {
        val api = CurrencyApiProvider.provideCurrencyApi()
        val response = api.getExchangeRate("USD/EUR")
        response.body()?.let {
            exchangeRate = it.rate
        }
    }
}