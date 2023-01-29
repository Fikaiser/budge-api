package hr.fika.budge.models.stock

import com.google.gson.annotations.SerializedName

data class StockBalance (
    @SerializedName("idStockBalance") val idStocksBalance: Int? = null,
    @SerializedName("tag") val tag: String? = null,
    @SerializedName("amount") val amount: Double? = null,
    @SerializedName("portfolioId")val portfolioId: Int? = null,
    @SerializedName("icon")val iconUrl: String? = null,
    @SerializedName("price") var price: Double? = null
)