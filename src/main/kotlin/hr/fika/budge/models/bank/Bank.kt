package hr.fika.budge.models.bank

import com.google.gson.annotations.SerializedName

data class Bank (
    @SerializedName("idBank") val idBank: Int,
    @SerializedName("name") val name: String,
)