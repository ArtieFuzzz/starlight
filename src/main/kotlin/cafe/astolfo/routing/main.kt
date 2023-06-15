package cafe.astolfo.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.MainRoute() {
    get("/") {
        call.respondText("Hello World!")
    }
}