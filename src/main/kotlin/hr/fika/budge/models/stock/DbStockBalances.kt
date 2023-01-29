package hr.fika.budge.models.stock

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object StockBalances : IntIdTable(columnName = "idstockbalance") {
    val portfolioId = integer("stockportfolioid").uniqueIndex().references(StockPortfolios.id)
    val tag = varchar("stocktag", 20)
    val amount = decimal("amount", 10, 5)
}

class DbStocksBalance(val idStockBalance: EntityID<Int>) : IntEntity(idStockBalance) {
    companion object : IntEntityClass<DbStocksBalance>(StockBalances)

    var portfolioId by StockBalances.portfolioId
    var tag by StockBalances.tag
    var amount by StockBalances.amount

    fun toStockBalance() =
        StockBalance(idStockBalance.value, tag, amount.toDouble(), portfolioId, StockTags.valueOf(tag).iconUrl)
}