package hr.fika.budge.models.bank

import hr.fika.budge.models.user.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


object BankAccounts : IntIdTable(columnName = "idbankaccount") {
    val userId = integer("userid").uniqueIndex().references(Users.id)
    val bankId = integer("bankid").uniqueIndex().references(Banks.id)
}
class DbBankAccount(val idBankAccount: EntityID<Int>) : IntEntity(idBankAccount) {
    companion object : IntEntityClass<DbBankAccount>(BankAccounts)

    var userId by BankAccounts.userId
    var bankId by BankAccounts.bankId

    fun toBankAccount() = BankAccount(idBankAccount.value, bankId)
}
