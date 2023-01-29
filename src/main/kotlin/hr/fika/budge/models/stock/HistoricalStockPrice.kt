package hr.fika.budge.models.stock

import com.google.gson.annotations.SerializedName

data class HistoricalStockPrice(
    @SerializedName("values") var values: List<HistoricalStockValues> = listOf(),
)
