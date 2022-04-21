package com.cows.services

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import java.util.*

val dotenv:Dotenv = dotenv {
    filename = "./src/main/resources/env.example"
}

val SimulationServerString: String = dotenv["Simulation-Server-String"] ?: "http://127.0.0.1:8080/cows"

data class SimulationServer(private val endpoint:String)

object ResourceManagementServer {

    private val SimulationEndpoints:List<SimulationServer>
    private var EndpointIterator: Iterator<SimulationServer>

    init{
        SimulationEndpoints = generateSimulationEndpoints(SimulationServerString)
        EndpointIterator = SimulationEndpoints.iterator()
    }
    private fun generateSimulationEndpoints(endpointsString:String): List<SimulationServer> {
        return Collections.synchronizedList(endpointsString.split("|").map { s:String -> SimulationServer(s) })
    }


    fun getSimulationServer() : SimulationServer{
        return if(EndpointIterator.hasNext()) EndpointIterator.next() else resetAndReturnEndpointIterator()
    }

    private fun resetAndReturnEndpointIterator(): SimulationServer{
        EndpointIterator = SimulationEndpoints.iterator()
        return EndpointIterator.next()
    }
}