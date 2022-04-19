package com.cows.game.serverConnection

import com.cows.game.serverConnection.shared.GameCreateResponse
import com.cows.game.serverConnection.shared.GameJoinResponse
import com.cows.game.serverConnection.shared.Message
import com.cows.game.serverConnection.shared.OpCode
import com.google.gson.GsonBuilder
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

internal class ServerConnectionTest {

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun testConnectionSetup(): Unit = runBlocking{
        val gson = GsonBuilder().setPrettyPrinting().create()
        val client1 = HttpClient(CIO) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            install(WebSockets)
            install(JsonFeature){
                serializer = GsonSerializer()
            }
        }

        val client2 = HttpClient(CIO) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            install(WebSockets)
            install(JsonFeature){
                serializer = GsonSerializer()
            }
        }
        launch {

            val response1: GameCreateResponse = client1.request("http://0.0.0.0:8080/game/create")
            val userId1 = response1.userId
            val gameJoinCode1 = response1.gameJoinCode
            val gameCodeUUID1 = response1.gameCodeUUID

            launch {
                val message:Message = Message(userId1,gameCodeUUID1,OpCode.CONNECT,null)
                val string:String = Message.gson.toJson(message)
                var isConnected = false
                client1.webSocket(method = HttpMethod.Get, host = "0.0.0.0", port = 8080, path = "/ws") {
                    send(Frame.Text(string))
                    while(!isConnected){
                        if (!incoming.isEmpty){
                            val incoming = incoming.receive() as Frame.Text
                            val recieveMessage = gson.fromJson(incoming.readText(),Message::class.java)
                            assertEquals(recieveMessage.opCode, OpCode.AWAIT)
                            isConnected = true
                        }
                    }
                    delay(5000L)
                    val incoming = incoming.receive() as Frame.Text
                    val recieveMessage = gson.fromJson(incoming.readText(),Message::class.java)
                    assertEquals(recieveMessage.opCode, OpCode.CONNECTED)
                    println("Client 1 connected")
                }
            }

            launch {
                delay(2000L)
                val response2: GameJoinResponse = client2.request("http://0.0.0.0:8080/game/join/${gameJoinCode1}")
                val userId2 = response2.userId
                val gameUUID2 = response2.gameCodeUUID

                val message:Message = Message(userId2,gameUUID2, OpCode.CONNECT,null)
                val string:String = Message.gson.toJson(message)
                var isConnected = false
                client2.webSocket(method = HttpMethod.Get, host = "0.0.0.0", port = 8080, path = "/ws") {
                    while(!isConnected){
                        send(Frame.Text(string))
                        if (!incoming.isEmpty){
                            val incoming = incoming.receive() as Frame.Text
                            val recieveMessage = gson.fromJson(incoming.readText(),Message::class.java)
                            assertEquals(recieveMessage.opCode, OpCode.CONNECTED)
                            if (recieveMessage.opCode == OpCode.CONNECTED){
                                isConnected = true
                            }
                        }
                    }
                    println("Client 2 connected")
                }
            }
        }
    }
}