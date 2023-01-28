package hr.fika.budge.models.bank

import com.google.gson.annotations.SerializedName

data class Transaction (
    @SerializedName("idTransaction") val id: Int? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("transactionTimestamp") val transactionTimestamp: Long? = null,
    @SerializedName("amount") val amount: Double? = null,
    @SerializedName("reoccurring") val reoccurring: Boolean? = null,
    @SerializedName("accountId") val accountId: Int? = null
)