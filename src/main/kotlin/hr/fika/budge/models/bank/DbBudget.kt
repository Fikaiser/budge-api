package hr.fika.budge.models.bank

import hr.fika.budge.models.user.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp

object Budgets : IntIdTable(columnName = "idbudget") {
    val description = varchar("description", 100)
    val transactionTimestamp = timestamp("budgetdate")
    val amount = decimal("amount", 10, 2)
    val userId = integer("userid").uniqueIndex().references(Users.id)
}
class DbBudget(val idBudget: EntityID<Int>) : IntEntity(idBudget) {
    companion object : IntEntityClass<DbBudget>(Budgets)

    var description by Budgets.description
    var budgetDate by Budgets.transactionTimestamp
    var amount by Budgets.amount
    var userId by Budgets.userId

    fun toBudget() = Budget(
        idBudget.value,
        description,
        budgetDate.toEpochMilli(),
        amount.toDouble(),
        userId
    )
}