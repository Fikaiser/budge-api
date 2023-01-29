package hr.fika.budge.models.stock

import com.google.gson.annotations.SerializedName

data class StockPortfolio (
    @SerializedName("idStockPortfolio") val idStockPortfolio: Int? = null,
    @SerializedName("userId")val userId: Int? = null
)