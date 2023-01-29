package hr.fika.budge.models.crypto

import hr.fika.budge.models.user.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object CryptoWallets : IntIdTable(columnName = "idcryptowallet") {
    val userId = integer("userid").uniqueIndex().references(Users.id)
}
class DbCryptoWallet(val idCryptoWallet: EntityID<Int>) : IntEntity(idCryptoWallet) {
    companion object : IntEntityClass<DbCryptoWallet>(CryptoWallets)

    var userId by CryptoWallets.userId

    fun toCryptoWallet() = CryptoWallet(idCryptoWallet.value, userId)
}