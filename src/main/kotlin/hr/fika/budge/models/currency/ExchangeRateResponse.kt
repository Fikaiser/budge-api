package hr.fika.budge.models.currency

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    @SerializedName("symbol") var symbol: String? = null,
    @SerializedName("rate") var rate: Double? = null,
    @SerializedName("timestamp") var timestamp: Int? = null
)