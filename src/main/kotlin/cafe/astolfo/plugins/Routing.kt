package cafe.astolfo.plugins

import cafe.astolfo.AuthorizationPayload
import cafe.astolfo.MessagePayload
import cafe.astolfo.auth.validateAndDecode
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.http.*
import io.ktor.server.application.*
import java.util.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }

        status(HttpStatusCode.NotFound) { call, status ->
            call.respondText("Not found", status = status)
        }
    }
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/image") {

        }
        post("/token/login") {
            var authHeader: String = call.request.headers["Authorization"] ?: ""
            authHeader = authHeader.split(" ")[1]
            println(authHeader)

            if (authHeader.isEmpty()) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    MessagePayload("No Authorization header", HttpStatusCode.BadRequest.value)
                )
            }

            if (validateAndDecode(authHeader)) {
                val jwtAudience = System.getenv("STARLIGHT_JWT_AUDIENCE")
                val jwtDomain = System.getenv("STARLIGHT_JWT_DOMAIN")
                // val jwtRealm = System.getenv("STARLIGHT_JWT_REALM")
                val jwtSecret = System.getenv("STARLIGHT_JWT_SECRET")

                val token = with(JWT.create()) {
                    withAudience(jwtAudience)
                    withIssuer(jwtDomain)
                    withExpiresAt(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                }.sign(Algorithm.HMAC512(jwtSecret))

                call.respond(AuthorizationPayload(token))
            } else {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    MessagePayload("Invalid Authorization", HttpStatusCode.BadRequest.value)
                )
            }
        }
    }
}
