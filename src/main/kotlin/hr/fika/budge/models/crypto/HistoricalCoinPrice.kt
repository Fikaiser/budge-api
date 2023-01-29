package hr.fika.budge.models.crypto

import com.google.gson.annotations.SerializedName

data class HistoricalCoinPrice(
    @SerializedName("change") var change: Double? = null,
    @SerializedName("history") var history: List<CoinPriceHistory> = listOf()
)
