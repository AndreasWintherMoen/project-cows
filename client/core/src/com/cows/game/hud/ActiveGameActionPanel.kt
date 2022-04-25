package com.cows.game.hud

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.cows.game.ClickSubscriber
import com.cows.game.controllers.TileController
import com.cows.game.controllers.TowerController
import com.cows.game.managers.RoundManager
import com.cows.game.models.TileModel
import com.cows.game.models.TowerModel
import com.cows.game.roundSimulation.rawJsonData.JsonTower

class ActiveGameActionPanel:ActionPanel(), ClickSubscriber {
    private var coins = UnitCounterPanel.calculateAvailableUnits()
    private var coinsText = FontObject(coins.toString(), 60, Vector2(this.position.x+68f, 500f))
    private val speedupButton = Button("Buttons/speedup-button.png", Vector2(this.position.x, 0f)) { toggleSpeed() }
    private var selectedTowerRadius: SmartObject? = null

    init {
        coinsText.zIndex = 3
        speedupButton.zIndex = 3
        subscribeToClickEvents()
    }

    private fun toggleSpeed() {
        if (RoundManager.useFastForward) normalspeed()
        else speedup()
    }

    private fun speedup() {
        RoundManager.useFastForward = true
        speedupButton.texture = Texture("Buttons/speedup-button-clicked.png")
    }

    private fun normalspeed() {
        RoundManager.useFastForward = false
        speedupButton.texture = Texture("Buttons/speedup-button.png")
    }

    override fun die() {
        super.die()
        speedupButton.die()
        coinsText.die()
        selectedTowerRadius?.die()
    }

    override fun dispose() {
        super.dispose()
        speedupButton.dispose()
        coinsText.dispose()
    }

    private fun deselectSelectedTowerRadius() {
        selectedTowerRadius?.hide = true
        selectedTowerRadius?.die()
        selectedTowerRadius = null
    }

    override fun click(position: Vector2, tile: TileController?) {
        if (tile == null) {
            deselectSelectedTowerRadius()
            return
        }

        val clickedTower = getTowerAtTile(tile)

        clickedTower?.let {
            deselectSelectedTowerRadius()
            selectedTowerRadius = SmartObject("Towers/attackRadius.png", tile.tileModel.coordinate.toVector2(), 0.25f + it.range * 0.5f)
            selectedTowerRadius!!.zIndex = 9
            selectedTowerRadius!!.sprite.setPosition(tile.tileModel.coordinate.toVector2().x - selectedTowerRadius!!.sprite.width/2 + TileModel.WIDTH/2, tile.tileModel.coordinate.toVector2().y - selectedTowerRadius!!.sprite.height/2 + TileModel.HEIGHT/2)
        } ?: run {
            deselectSelectedTowerRadius()
        }
    }

    private fun getTowerAtTile(tile: TileController): TowerModel? {
        return RoundManager.roundSimulation?.towerList?.find { it.position == tile.tileModel.coordinate }?.toTowerModel()
    }
}