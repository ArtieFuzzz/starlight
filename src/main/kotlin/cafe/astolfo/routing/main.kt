package cafe.astolfo.routing

import cafe.astolfo.MessagePayload
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Route.mainRoute() {
    get("/") {
        call.respond(
            MessagePayload(
                "Welcome to Starlight! Checkout the repo at @ github.com/ArtieFuzzz/starlight",
                HttpStatusCode.OK.value
            )
        )
    }
    staticFiles(".well-known", File("certs")) {
    }
}