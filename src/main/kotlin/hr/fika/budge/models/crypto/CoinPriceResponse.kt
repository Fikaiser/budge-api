package hr.fika.budge.models.crypto

import com.google.gson.annotations.SerializedName

data class CoinPriceResponse (
    @SerializedName("status") var status : String? = null,
    @SerializedName("data") var data : CoinPriceResponseData?   = CoinPriceResponseData()
)

data class CoinPriceResponseData (
    @SerializedName("price") var price: String? = null,
    @SerializedName("timestamp") var timestamp : Int?    = null
)