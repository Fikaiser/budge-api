package hr.fika.budge

import hr.fika.budge.plugins.*
import hr.fika.budge.utils.EncryptionManager
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    EncryptionManager.initEncryption()
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)

}

fun Application.module() {
    configureSerialization()
    configureSecurity()
    configureRouting()
}
