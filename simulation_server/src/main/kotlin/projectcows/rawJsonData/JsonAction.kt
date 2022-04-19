package projectcows.rawJsonData

import projectcows.enums.ActionType

data class JsonAction (
    val subject: Int,
    val verb: ActionType,
    val obj: Int?
)

