package com.cows.game.serverConnection

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*


data class GameCreateResponse(
    val userId: UUID,
    val gameJoinCode: String,
    val gameCodeUUID: UUID,
)

data class GameJoinResponse(
    val userId: UUID,
    val gameCodeUUID: UUID,
)

object ServerConnection {
    private val client = HttpClient(CIO) {
        install(JsonFeature){
            serializer = GsonSerializer()
        }
    }
    var userId: UUID? = null
    var gameJoinCode: String? = null
    var gameCodeUUID: UUID? = null

    // User will automatically join game after creating it
    // TODO: Send user to a websocket connection to check when user 2 joins the game
    suspend fun createGame(): GameCreateResponse {
        val response: GameCreateResponse = client.request("http://0.0.0.0:8080/game/create")
        userId = response.userId
        gameJoinCode = response.gameJoinCode
        gameCodeUUID = response.gameCodeUUID
        return response
    }

    // TODO: Send user to a websocket connection to automatically get game updates
    suspend fun joinGame(gameJoinCode: String): GameJoinResponse {
        val response: GameJoinResponse = client.request("http://0.0.0.0:8080/game/join/${gameJoinCode}")
        userId = response.userId
        gameCodeUUID = response.gameCodeUUID
        return response
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun testClientConnection() = runBlocking{
        launch {
            val client1 = HttpClient(CIO) {
                install(WebSockets)
                install(JsonFeature){
                    serializer = GsonSerializer()
                }
            }

            val client2 = HttpClient(CIO) {
                install(WebSockets)
                install(JsonFeature){
                    serializer = GsonSerializer()
                }
            }

            val response1: GameCreateResponse = client1.request("http://0.0.0.0:8080/game/create")
            val userId1 = response1.userId
            val gameJoinCode1 = response1.gameJoinCode
            val gameCodeUUID1 = response1.gameCodeUUID



            val response2: GameCreateResponse = client2.request("http://0.0.0.0:8080/game/join/${gameJoinCode1}")
            val userId2 = response2.userId
            val gameCodeUUID2 = response2.gameCodeUUID
            println(response1)
            println(response2)



            client1.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/connect") {
                var isConnected = false
                while(!isConnected){
                    send(Frame.Text("${userId1}:${gameCodeUUID1}"))
                    if (!incoming.isEmpty){
                        val incoming = incoming.receive() as Frame.Text
                        if (incoming.readText() == "You are now logged in!"){
                            isConnected = true
                        }
                    }
                }


            }
        }


    }

    fun clearGameData() {
        userId = null
        gameJoinCode = null
        gameCodeUUID = null
    }
}