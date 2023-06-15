package cafe.astolfo

import cafe.astolfo.plugins.*
import cafe.astolfo.s3.R2Client
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking
import java.util.*

fun main(): Unit = runBlocking {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() = runBlocking {
    val s3 = R2Client()
    s3.start()
    s3.populateCache()

    Timer().scheduleAtFixedRate(object : TimerTask() {
        override fun run() {
            println("Cache Updated!")
            return runBlocking {
                s3.populateCache()
            }
        }
    }, 0, 1000 * 60 * 60 * 12)

    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    configureRouting(s3)
}
