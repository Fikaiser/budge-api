package hr.fika.budge.models.crypto

import com.google.gson.annotations.SerializedName

data class CryptoBalance (
    @SerializedName("idCryptoBalance") val idCryptoBalance: Int? = null,
    @SerializedName("tag") val tag: String? = null,
    @SerializedName("amount") val amount: Double? = null,
    @SerializedName("walletId")val walletId: Int? = null,
    @SerializedName("icon")val iconUrl: String? = null,
    @SerializedName("price") var price: Double? = null
)