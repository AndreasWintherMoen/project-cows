package com.cows.game.views

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.cows.game.enums.TileType
import com.cows.game.models.Tile

class TileView(private val model: Tile) : Renderable() {
   companion object {
      fun tileTypeToTexture(tileType: TileType): Texture =
         when(tileType) {
            TileType.GRASS -> Texture("grass.png")
            TileType.PATH -> Texture("path.png")
            else -> throw Error("Could not find tile type $tileType")
         }
   }

   private val texture = tileTypeToTexture(model.type)

   override fun render(batch: SpriteBatch, deltaTime: Float) {
      val pixel = model.coordinate.toPixel()
      batch.draw(texture, pixel.x, pixel.y)
   }

   override fun dispose() {
      texture.dispose()
   }
}