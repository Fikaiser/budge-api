package hr.fika.budge.models.user

import com.google.gson.annotations.SerializedName
import hr.fika.budge.models.bank.BankAccount

data class User (
    @SerializedName("idUser") val idUser: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("token") val apiToken: String,
    @SerializedName("bankAccount") val bankAccount: BankAccount? = null,
    @SerializedName("wallet") val cryptoWalletId: Int? = null,
    @SerializedName("portfolio") val stockPortfolioId: Int? = null,
)