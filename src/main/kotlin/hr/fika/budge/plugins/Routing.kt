package hr.fika.budge.plugins


import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import hr.fika.budge.JwtSecrets
import hr.fika.budge.dal.db.DatabaseProvider
import hr.fika.budge.models.user.DbUser
import hr.fika.budge.models.user.Users
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import java.util.concurrent.TimeUnit


fun Application.configureRouting() {
    routing {

        get("/test") {
            var text = "Hello from API"
            call.respondText(text)
        }

        post ("/login") {
            val params = call.request.queryParameters
            val email = params["email"]
            val hash = params["hash"]
            var user : DbUser? = null

            if (!email.isNullOrBlank() && !hash.isNullOrBlank()) {
                transaction(DatabaseProvider.provideDb()) {
                    SchemaUtils.create(Users)
                    val dbUser = DbUser.find { Users.email like email }.firstOrNull()
                    if (dbUser != null && dbUser.passHash == hash) {
                        user = dbUser
                    }
                }
                if (user != null) {
                    val token = JWT.create()
                        .withAudience(JwtSecrets.AUDIENCE.value)
                        .withIssuer(JwtSecrets.ISSUER.value)
                        .withClaim("username", "username")
                        .withExpiresAt(Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(45)))
                        .sign(Algorithm.HMAC256(JwtSecrets.SECRET.value))
                    // call.respond(hashMapOf("token" to token))
                    call.respondText(token)
                } else {
                    call.respondText(status = HttpStatusCode.BadRequest, text = "Such user not found")
                }
            } else {
                call.respondText(status = HttpStatusCode.BadRequest, text = "")
            }

        }

        post("/register") {
            val params = call.request.queryParameters
            val nickname = params["nickname"]
            val email = params["email"]
            val hash = params["hash"]

            if (!nickname.isNullOrBlank() && !email.isNullOrBlank() && !hash.isNullOrBlank()) {
                transaction(DatabaseProvider.provideDb()) {
                    SchemaUtils.create(Users)
                    DbUser.new {
                        this.nickname = nickname
                        this.email = email
                        this.passHash = hash
                    }
                }
                call.respondText("Added")
            } else {
                call.respondText("Failed")
            }
        }

        authenticate ("auth") {
            get("/hello") {
                call.respondText("Hello world")
            }
        }
    }
}
