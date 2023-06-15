package cafe.astolfo.routing

import cafe.astolfo.ImagePayload
import cafe.astolfo.s3.R2Client
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.imagesRoute(r2: R2Client) {
    get("/image") {
        val alt = call.request.queryParameters["alt"] ?: "false"
        val image = r2.getRandom(alt.toBoolean())

        call.respond(status = HttpStatusCode.OK, ImagePayload(image!!))
    }
}