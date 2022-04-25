package models.simulation.json

import models.enums.ActionType

data class JsonAction (
    val subject: Int,
    val verb: ActionType,
    val obj: Int?
)

