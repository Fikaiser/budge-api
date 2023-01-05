package hr.fika.budge.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import hr.fika.budge.JwtSecrets
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity() {
    authentication {
        jwt("auth") {
            realm = JwtSecrets.REALM.value
            val audience = JwtSecrets.AUDIENCE.value
            verifier(
                JWT
                .require(Algorithm.HMAC256(JwtSecrets.SECRET.value))
                .withAudience(audience)
                .withIssuer(JwtSecrets.ISSUER.value)
                .build())

            validate { credential ->
                if (credential.payload.audience.contains(audience)) JWTPrincipal(credential.payload) else null
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
        /*jwt {
                val jwtAudience = this@configureSecurity.environment.config.property("jwt.audience").getString()
                realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
                verifier(
                    JWT
                        .require(Algorithm.HMAC256("secret"))
                        .withAudience(jwtAudience)
                        .withIssuer(this@configureSecurity.environment.config.property("jwt.domain").getString())
                        .build()
                )
                validate { credential ->
                    if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
                }
            }*/
    }
}
