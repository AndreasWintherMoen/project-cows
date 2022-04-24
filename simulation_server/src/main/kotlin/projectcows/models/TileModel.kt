package projectcows.models

import projectcows.enums.TileType

data class TileModel(
    val type: TileType,
    val coordinate: Coordinate,
    )