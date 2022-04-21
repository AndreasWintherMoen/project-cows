package com.cows.services

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

val dotenv:Dotenv = dotenv {
    filename = "./src/main/resources/env.example"
}

val SimulationServerString: String = dotenv["Simulation-Server-String"] ?: "http://127.0.0.1:8080/cows"

data class SimulationServer(val endpoint:String)

data class SimulationServerNode(val simulationServer:SimulationServer, val nextNode:SimulationServer)

object ResourceManagementServer {

    private val SimulationEndpoints:List<SimulationServerNode>
    private var CurrentSimulationServerNode:SimulationServerNode

    init{
        SimulationEndpoints = generateSimulationEndpoints(SimulationServerString)
        CurrentSimulationServerNode = SimulationEndpoints[0]
    }
    private fun generateSimulationEndpoints(endpointsString:String): List<SimulationServerNode> {
        val endpointList = endpointsString.split("|").map { s:String -> SimulationServer(s) }
        var lengthCounter = 0
        val SimulationServerList = mutableListOf<SimulationServerNode>()

        for (endpoint in endpointList){
            if (lengthCounter < endpointList.lastIndex){
                SimulationServerList.add(SimulationServerNode(endpoint,endpointList[lengthCounter+1]))
                lengthCounter += 1
            } else{
                SimulationServerList.add(SimulationServerNode(endpoint,endpointList[0]))
            }
        }

        return Collections.synchronizedList(SimulationServerList)
    }


    fun getSimulationServer() : String{
        return CurrentSimulationServerNode.nextNode.endpoint
    }
}