package com.cows.services.simulation.models.json

import projectcows.rawJsonData.JsonUnit

data class JsonAvailableUnits (
    val fireUnit: JsonUnit,
    val waterUnit: JsonUnit,
    val grassUnit: JsonUnit,
)