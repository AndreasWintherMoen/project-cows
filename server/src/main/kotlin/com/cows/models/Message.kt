package com.cows.models

import java.util.*

// TODO: Implement a more intuitive separation between connect and join
enum class OpCode{
    CONNECT,
    JOIN,
    AWAIT,
    CONNECTED,
    INSTRUCTIONLOG,
    EVENTLOG,
}


// Todo: Implement data in its own class.
// Todo: Split Message into gameMessage and gameCodeMessage
data class Message(val userUUID: UUID,val gameUUID:UUID, val opCode:OpCode, val data:String?)