package com.cows.plugins

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {
    routing {
        // Give user UUID
        // Discuss if we need to check if user has already gotten UUID
        get("/user/id") {
            call.response.header("","")
            call.respond("hello")
        }
    }
}
