package hr.fika.budge.models.crypto

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object CryptoBalances : IntIdTable(columnName = "idcryptobalance") {
    val walletId = integer("cryptowalletid").uniqueIndex().references(CryptoWallets.id)
    val tag = varchar("cointag", 20)
    val amount = decimal("amount", 10, 5)
}

class DbCryptoBalance(val idCryptoBalance: EntityID<Int>) : IntEntity(idCryptoBalance) {
    companion object : IntEntityClass<DbCryptoBalance>(CryptoBalances)

    var walletId by CryptoBalances.walletId
    var tag by CryptoBalances.tag
    var amount by CryptoBalances.amount

    fun toCryptoBalance() =
        CryptoBalance(idCryptoBalance.value, tag, amount.toDouble(), walletId, CoinTags.valueOf(tag).icon)
}
