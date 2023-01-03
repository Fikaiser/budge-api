package hr.fika.budge.models.crypto

import com.google.gson.annotations.SerializedName

data class CoinHistoryResponse(
    @SerializedName("status") var status: String? = null,
    @SerializedName("data") var data: CoinHistoryData? = CoinHistoryData()
)

data class CoinHistoryData(
    @SerializedName("change") var change: String? = null,
    @SerializedName("history") var history: ArrayList<CoinPriceHistory> = arrayListOf()
)

data class CoinPriceHistory(
    @SerializedName("price") var price: String? = null,
    @SerializedName("timestamp") var timestamp: Int? = null
)