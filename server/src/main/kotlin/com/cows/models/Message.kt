package com.cows.models

import java.util.*

enum class OpCode{
    CONNECT,
    AWAIT,
    CONNECTED,
    INSTRUCTIONLOG,
    EVENTLOG,
}

data class Message(val userUUID: UUID,val gameUUID:UUID, val opCode:OpCode, val data:String?)