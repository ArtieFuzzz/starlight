package cafe.astolfo.plugins

import cafe.astolfo.routing.authorizationRoute
import cafe.astolfo.routing.imagesRoute
import cafe.astolfo.routing.mainRoute
import cafe.astolfo.s3.R2Client
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(r2: R2Client) {
    routing {
        mainRoute()
        imagesRoute(r2)
        authorizationRoute()
    }
}
