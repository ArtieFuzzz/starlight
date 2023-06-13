package cafe.astolfo

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import cafe.astolfo.plugins.*
import kotlinx.coroutines.runBlocking
import cafe.astolfo.s3.R2Client
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

fun main(): Unit = runBlocking {
    val s3 = R2Client()
    s3.start()

    s3.populateCache()

        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                println("Cache Updated!")
                return runBlocking { s3.populateCache() }
            }
        }, 0, 1000) // * 60 * 60 * 12


    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    configureRouting()
}
