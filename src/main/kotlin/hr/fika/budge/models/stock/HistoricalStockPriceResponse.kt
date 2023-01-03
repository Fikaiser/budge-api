package hr.fika.budge.models.stock

import com.google.gson.annotations.SerializedName

data class HistoricalStockPriceResponse(
    @SerializedName("meta") var meta: StockMetaData? = StockMetaData(),
    @SerializedName("values") var values: ArrayList<HistoricalStockValues> = arrayListOf(),
    @SerializedName("status") var status: String? = null
)

data class StockMetaData(
    @SerializedName("symbol") var symbol: String? = null,
    @SerializedName("interval") var interval: String? = null,
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("exchange_timezone") var exchangeTimezone: String? = null,
    @SerializedName("exchange") var exchange: String? = null,
    @SerializedName("mic_code") var micCode: String? = null,
    @SerializedName("type") var type: String? = null
)

data class HistoricalStockValues(
    @SerializedName("datetime") var datetime: String? = null,
    @SerializedName("open") var open: String? = null,
    @SerializedName("high") var high: String? = null,
    @SerializedName("low") var low: String? = null,
    @SerializedName("close") var close: String? = null,
    @SerializedName("volume") var volume: String? = null
)