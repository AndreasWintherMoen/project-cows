package connection

import com.google.gson.GsonBuilder
import io.ktor.websocket.*
import java.util.*

enum class OpCode{
    CONNECT,
    AWAIT,
    CONNECTED,
    INSTRUCTIONLOG,
    EVENTLOG,
    GAMESTATE,
}

data class Message(val userUUID: UUID, val gameUUID: UUID, val opCode: OpCode, val data:String?){

    companion object{
        val gson = GsonBuilder().setPrettyPrinting().create()
        fun generateWSFrame(message: Message): Frame {
            return Frame.Text(
                gson.toJson(
                    message
                )
            )
        }

        fun retrieveWSMessage(frame: Frame.Text): Message?{
            return gson.fromJson(frame.readText(), Message::class.java)
        }
    }

}