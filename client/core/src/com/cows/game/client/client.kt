package com.cows.game.client


import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.runBlocking
import io.ktor.client.features.auth.*
class Client {

    fun sendRequest() = runBlocking {
        launch {
            val client = HttpClient(CIO){
                install(Auth){

                }

            }
            val response: HttpResponse = client.post("http://0.0.0.0:8080/login"){
                bo
            }
            val res:String = response.receive()
            print("\n $res.,----\n")
        }
    }

    init {
        sendRequest()
    }



}