package hr.fika.budge.models.stock

import com.google.gson.annotations.SerializedName

data class StockPriceResponse(
    @SerializedName("price") var price: String? = null
)
