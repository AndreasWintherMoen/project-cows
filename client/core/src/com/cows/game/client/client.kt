package com.cows.game.client


import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.runBlocking

class client {

    fun sendRequest() = runBlocking {
        launch {
            val client = HttpClient(CIO)
            val response: HttpResponse = client.get("http://0.0.0.0:8080/")
            val res:String = response.receive()
            print("\n $res.,----\n")
        }
    }

    init {
        sendRequest()
    }

}