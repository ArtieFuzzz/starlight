package cafe.astolfo.routing

import cafe.astolfo.AuthorizationPayload
import cafe.astolfo.MessagePayload
import cafe.astolfo.auth.basic.verify
import cafe.astolfo.auth.jwt.signToken
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authorizationRoute() {
    post("/token") {
        val username = call.request.queryParameters["username"] ?: call.request.queryParameters["user"] ?: "some-user"
        var authHeader: String = call.request.headers["Authorization"] ?: ""
        authHeader = authHeader.split(" ")[1]

        if (authHeader.isEmpty()) {
            call.respond(
                status = HttpStatusCode.BadRequest,
                MessagePayload("No Authorization header", HttpStatusCode.BadRequest.value)
            )
        }

        if (verify(authHeader)) {
            val token = signToken(username)

            call.respond(AuthorizationPayload(token))
        } else {
            call.respond(
                status = HttpStatusCode.BadRequest,
                MessagePayload("Invalid Authorization", HttpStatusCode.BadRequest.value)
            )
        }
    }
}