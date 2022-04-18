package projectcows

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import projectcows.plugins.*

fun main() {
    embeddedServer(Netty, port = 8069, host = "0.0.0.0") {
        configureRouting()
    }.start(wait = true)
}
