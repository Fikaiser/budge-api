package hr.fika.budge.models.bank

import com.google.gson.annotations.SerializedName

data class AtmLocation (
    @SerializedName("idLocation") val id: Int? = null,
    @SerializedName("street") val street: String? = null,
    @SerializedName("latitude") val latitude: Double? = null,
    @SerializedName("longitude") val longitude: Double? = null,
    @SerializedName("bankId") val bankId: Int? = null
)