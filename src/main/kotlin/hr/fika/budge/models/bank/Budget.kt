package hr.fika.budge.models.bank

import com.google.gson.annotations.SerializedName

data class Budget (
    @SerializedName("idBudget") val idBudget: Int? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("budgetDate") val budgetDate: Long? = null,
    @SerializedName("amount") val amount: Double? = null,
    @SerializedName("userId") val userId: Int? = null
)