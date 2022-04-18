package projectcows.models

import com.cows.game.enums.TileType

data class TileModel(
    val type: TileType,
    val coordinate: Coordinate,
    ) {
    companion object {
        const val WIDTH = 64f
        const val HEIGHT = 64f
    }
}