package projectcows

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import projectcows.plugins.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.gson.*
import java.text.DateFormat

fun main() {
    embeddedServer(Netty, port = 8069, host = "0.0.0.0") {
        configureRouting()

        install(ContentNegotiation) {
            gson {
                setDateFormat(DateFormat.LONG)
                setPrettyPrinting()
            }
        }
    }.start(wait = true)
}
