package hr.fika.budge.plugins

import hr.fika.budge.dal.crypto.CoinApiProvider
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*


fun Application.configureRouting() {
    routing {
        get("/") {

            val coinApi = CoinApiProvider.provideCoinApi()

            val response = coinApi.getCoinPrice("Qwsogvtv82FCd")
            val myResponse = response.body()
            var text = ""
            myResponse?.let {
                it.data?.let { data ->
                    text = data.price.toString()
                }
            }

            call.respondText(text)
        }
    }
}
