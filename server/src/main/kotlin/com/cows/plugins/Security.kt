package com.cows.plugins

import io.ktor.server.sessions.*
import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import java.util.UUID

// TODO: Implement a proper level of security. For now, the user only needs a username to log in.
fun Application.configureSecurity() {
    install(Authentication){

    }
}

