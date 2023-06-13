package cafe.astolfo.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.http.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
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
    }
}
