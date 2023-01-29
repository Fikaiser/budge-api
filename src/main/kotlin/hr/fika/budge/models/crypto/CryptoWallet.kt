package hr.fika.budge.models.crypto

import com.google.gson.annotations.SerializedName

data class CryptoWallet (
    @SerializedName("idCryptoWallet") val idWallet: Int? = null,
    @SerializedName("userId")val userId: Int? = null
)