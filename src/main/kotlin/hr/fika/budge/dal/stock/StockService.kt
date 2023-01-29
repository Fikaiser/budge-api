package hr.fika.budge.dal.stock

import hr.fika.budge.dal.db.DatabaseProvider
import hr.fika.budge.models.stock.*
import hr.fika.budge.models.user.Users
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.random.Random

object StockService {
    fun getStockPortfolio(userId: Int): StockPortfolio? {
        var portfolio: StockPortfolio? = null
        transaction(DatabaseProvider.provideDb()) {
            SchemaUtils.create(StockPortfolios)
            val check = DbStockPortfolio.find { StockPortfolios.userId eq userId }.toList()
            check.firstOrNull()?.let { portfolio = it.toStockPortfolio() }
        }
        return portfolio
    }

    fun registerStockPortfolio(userId: Int): StockPortfolio {
        var portfolio = StockPortfolio()
        transaction(DatabaseProvider.provideDb()) {
            SchemaUtils.create(Users)
            SchemaUtils.create(StockPortfolios)
            SchemaUtils.create(StockBalances)
            val check = DbStockPortfolio.find { StockPortfolios.userId eq userId }.toList()
            if (check.isEmpty()) {
                portfolio = DbStockPortfolio.new {
                    this.userId = userId
                }.toStockPortfolio()
                commit()
                for (i in 1..3) {
                    generateStockBalance(portfolio.idStockPortfolio!!)
                }
            } else {
                portfolio = check.first().toStockPortfolio()
            }
        }
        return portfolio
    }

    fun getStockBalances(portfolioId: Int): List<StockBalance> {
        val list = mutableListOf<StockBalance>()
        transaction(DatabaseProvider.provideDb()) {
            SchemaUtils.create(StockBalances)
            DbStocksBalance.find { StockBalances.portfolioId eq portfolioId }.forEach { dbStockBalance ->
                list.add(dbStockBalance.toStockBalance())
            }
        }
        return list
    }

    private fun generateStockBalance(idPortfolio: Int) {
        val stock = StockTags.values().toList().shuffled().first()
        transaction(DatabaseProvider.provideDb()) {
            SchemaUtils.create(StockBalances)
            DbStocksBalance.new {
                this.tag = stock.name
                amount = Random.nextDouble(0.000, 3.000).toBigDecimal()
                portfolioId = idPortfolio
            }
        }
    }
}