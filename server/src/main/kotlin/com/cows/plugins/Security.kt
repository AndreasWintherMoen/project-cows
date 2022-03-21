package com.cows.plugins

import io.ktor.server.sessions.*
import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import java.util.UUID

data class UserSession(val userName: String, val count: Int): Principal

/*data class UserSession(val user:UUID): Principal*/
private val UUIDMap = mutableSetOf<UUID>()




// TODO: Implement a proper level of security. For now, the user only needs a username to log in.
fun Application.configureSecurity() {
    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 3600 // Cookie is valid for one hour
        }
    }
    install(Authentication) {

        form("auth_form"){
            userParamName = "Username"
            passwordParamName = "Password"
            validate { credentials ->
                if (credentials.name == "Test" && credentials.password == "Testesen"){
                    UserIdPrincipal(credentials.name)
                }
                else {
                    null
                }
            }
        }
        session<UserSession>("auth_session"){
            validate { session ->
                if (session.userName.startsWith("jet")){
                    session
                } else{
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
            val newUserUUID = UUID.randomUUID()
            call.respond(newUserUUID)
            UUIDMap.add(newUserUUID)
        }

        authenticate ("auth_form"){
            post ("/login"){
                val userName = call.principal<UserIdPrincipal>()?.name
                call.sessions.set(userName?.let { it1 -> UserSession(userName = it1, count = 1) })
                call.respondText("hello, ${userName}")
                call.respondRedirect("/auth")
            }
        }

        authenticate("auth_session"){
            get("/auth") {
                val userSession = call.principal<UserSession>()
                call.sessions.set(userSession?.copy(count = userSession.count+1))
                call.respondText("Hello, ${userSession?.userName}! Visit count is ${userSession?.count}.")
            }
        }


    }
}

