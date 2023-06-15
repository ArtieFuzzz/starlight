package cafe.astolfo.plugins

import cafe.astolfo.MessagePayload
import cafe.astolfo.routing.authorizationRoute
import cafe.astolfo.routing.imagesRoute
import cafe.astolfo.routing.mainRoute
import cafe.astolfo.s3.R2Client
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(r2: R2Client) {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(
                text = "500: $cause\n\nPlease report this error to `hey@astolfo.cafe`",
                status = HttpStatusCode.InternalServerError
            )
        }

        status(HttpStatusCode.NotFound) { call, status ->
            call.respond(status = status, MessagePayload("Route not found", status.value))
        }
    }
    routing {
        mainRoute()
        imagesRoute(r2)
        authorizationRoute()
    }
}
