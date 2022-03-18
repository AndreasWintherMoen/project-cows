package com.cows.plugins

import io.ktor.server.sessions.*
import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import java.security.Principal

data class UserSession(val userName: String, val count: Int): Principal {
    override fun getName(): String {
        return this.userName
    }
}

// TODO: Implement a proper level of security. For now, the user only needs a username to log in.
fun Application.configureSecurity() {
    data class MySession(val count: Int = 0)
    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 3600 // Cookie is valid for one hour
        }
    }
    install(Authentication) {

        form("auth_form"){
            validate { credentials ->
                print(credentials)
                if (credentials.name == "Test"){
                    UserIdPrincipal(credentials.name)
                }
                else {
                    null
                }
            }
        }
        session<UserSession>("auth_session"){
            validate { session ->

                if (session.name.startsWith("jet")){
                    UserIdPrincipal(session.name)
                } else{
                    // TODO: Implement giving user a new token
                    null
                }
            }
            challenge {
                call.respondRedirect("/login")
            }

        }
    }


    routing {

        get("/login"){
            call.request.header("userName")

        }
        authenticate ("auth_form"){
            post ("/login"){
                print(call.request)
                val userName = call.principal<UserIdPrincipal>()?.name
                call.sessions.set(userName?.let { it1 -> UserSession(userName = it1, count = 1) })
                print(userName)
                call.respondText("hello, ${userName}")

            }
        }
        get("/session/increment") {
            val session = call.sessions.get<MySession>() ?: MySession()
            call.sessions.set(session.copy(count = session.count + 1))
            call.respondText("Counter is ${session.count}. Refresh to increment.")
        }
    }
}

