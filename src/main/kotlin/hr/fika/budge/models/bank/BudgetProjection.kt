package hr.fika.budge.models.bank

import com.google.gson.annotations.SerializedName

data class BudgetProjection (
    @SerializedName("budget") val budget: Budget,
    @SerializedName("change") val change: Double,
    @SerializedName("isOnTarget") val isOnTarget: Boolean
)