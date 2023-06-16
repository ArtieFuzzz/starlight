package cafe.astolfo.plugins

import cafe.astolfo.MessagePayload
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(
                status = HttpStatusCode.InternalServerError,
                MessagePayload(
                    status = HttpStatusCode.InternalServerError.value,
                    message = "$cause\n\nPlease report this error to `hey@astolfo.cafe`"
                )
            )
        }

        status(HttpStatusCode.NotFound) { call, status ->
            call.respond(status = status, MessagePayload("Route not found", status.value))
        }
        status(HttpStatusCode.Forbidden) { call, status ->
            call.respond(status = status, MessagePayload("You are Forbidden here", status.value))
        }
        status(HttpStatusCode.Unauthorized) { call, status ->
            call.respond(status = status, MessagePayload("Invalid Authorization (JWT Token)", status.value))
        }
        status(HttpStatusCode.Processing) { call, status ->
            call.respond(status = status, MessagePayload("Processing, hold up", status.value))
        }
        status(HttpStatusCode.UnprocessableEntity) { call, status ->
            call.respond(status = status, MessagePayload("Unprocessable", status.value))
        }
    }
}