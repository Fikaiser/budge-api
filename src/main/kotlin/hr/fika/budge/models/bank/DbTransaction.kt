package hr.fika.budge.models.bank

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

object Transactions : IntIdTable(columnName = "idtransaction") {
    val description = varchar("description", 100)
    val transactionDate = date("transactiondate")
    val amount = decimal("amount", 10, 2)
    val reoccurring = bool("reoccurring")
    val accountId = integer("accountid").uniqueIndex().references(BankAccounts.id)
}
class DbTransaction(val idTransaction: EntityID<Int>) : IntEntity(idTransaction) {
    companion object : IntEntityClass<DbTransaction>(Transactions)

    var description by Transactions.description
    var transactionDate by Transactions.transactionDate
    var amount by Transactions.amount
    var reoccurring by Transactions.reoccurring
    var accountId by Transactions.accountId

    fun toTransaction() = Transaction(
        idTransaction.value,
        description,
        transactionDate,
        amount.toDouble(),
        reoccurring,
        accountId
    )
}