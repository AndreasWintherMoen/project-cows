package com.cows

import io.ktor.server.application.*
import com.cows.plugins.*
import io.ktor.websocket.*
import java.util.concurrent.atomic.AtomicInteger

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureRouting()
    configureSecurity()
    /*configureHTTP()*/
    /*configureMonitoring()*/
    /*configureSerialization()*/

    configureSockets()
}
