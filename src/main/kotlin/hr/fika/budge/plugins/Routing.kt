package hr.fika.budge.plugins

import hr.fika.budge.dal.stock.StockApiProvider
import hr.fika.budge.models.stock.StockIntervals
import hr.fika.budge.utils.CurrencyUtils
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*


fun Application.configureRouting() {
    routing {
        get("/") {

            val api = StockApiProvider.provideStockApi()

            val response = api.getHistoricalPrices(tag = "AAPL", outputSize = 7, interval = StockIntervals.DAY.value)
            val myResponse = response.body()
            var text = ""
            myResponse?.let {
                it.values.forEach { value ->
                    text += "DATE - ${value.datetime} PRICE - ${CurrencyUtils.convertUsdToEur(value.close!!.toDouble())} \n"
                }
            }
            call.respondText(text)
        }
    }
}
