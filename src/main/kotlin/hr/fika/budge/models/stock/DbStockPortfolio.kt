package hr.fika.budge.models.stock

import hr.fika.budge.models.user.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object StockPortfolios : IntIdTable(columnName = "idstockportfolio") {
    val userId = integer("userid").uniqueIndex().references(Users.id)
}
class DbStockPortfolio(val idStockPortfolio: EntityID<Int>) : IntEntity(idStockPortfolio) {
    companion object : IntEntityClass<DbStockPortfolio>(StockPortfolios)

    var userId by StockPortfolios.userId

    fun toStockPortfolio() = StockPortfolio(idStockPortfolio.value, userId)
}