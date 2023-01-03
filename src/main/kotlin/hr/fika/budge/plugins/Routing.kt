package hr.fika.budge.plugins

import hr.fika.budge.dal.crypto.CoinApiProvider
import hr.fika.budge.dal.crypto.MONTH
import hr.fika.budge.utils.epochToDateTime
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*


fun Application.configureRouting() {
    routing {
        get("/") {

            val coinApi = CoinApiProvider.provideCoinApi()

            val response = coinApi.getHistoricalPrice(tag = "Qwsogvtv82FCd", period = MONTH)
            val myResponse = response.body()
            var text = ""
            myResponse?.let {
                it.data?.let { data ->
                    data.history.forEach { price ->
                        text += "Date - ${price.timestamp!!.epochToDateTime()} Price - ${price.price}\n"
                    }
                }
            }
            call.respondText(text)
        }
    }
}
