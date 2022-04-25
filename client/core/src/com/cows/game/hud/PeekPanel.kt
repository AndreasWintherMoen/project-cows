package com.cows.game.hud

import com.badlogic.gdx.math.Vector2
import com.cows.game.Application
import com.cows.game.enums.UnitType
import com.cows.game.managers.RoundManager

class PeekPanel {
    private val defaultPosition = Vector2(Application.WIDTH - ActionPanel.WIDTH - 10f, 201f)
    private val expandButton = Button("HUD/PeekPanel/expand-button.png", Vector2(defaultPosition.x - 62f, Application.HEIGHT - 93f))
    private val collapseButton = Button("HUD/PeekPanel/collapse-button.png", Vector2(defaultPosition.x - 62f - 180f, Application.HEIGHT - 93f))
    private val panel = SmartObject("HUD/PeekPanel/peek-panel.png", defaultPosition, 1f)

    private fun getUnitName(type: UnitType, level: Int): String {
        when (type) {
            UnitType.NONE -> return ""
            UnitType.FIRE -> return when (level) {
                0 -> "charmander"
                1 -> "charmeleon"
                2 -> "charizard"
                else -> ""
            }
            UnitType.WATER -> return when (level) {
                0 -> "squirtle"
                1 -> "wartortle"
                2 -> "blastoise"
                else -> ""
            }
            UnitType.GRASS -> return when (level) {
                0 -> "bulbasaur"
                1 -> "ivysaur"
                2 -> "venosaur"
                else -> ""
            }
        }
    }

    private val coins = if (RoundManager.playerCreatedGame!!) RoundManager.gameStatus!!.playerStates.second.coins else RoundManager.gameStatus!!.playerStates.first.coins
//    private val coins = RoundManager.playerCreatedGame?.let { if (it) RoundManager.gameStatus!!.playerStates.second.coins else RoundManager.gameStatus!!.playerStates.first.coins } if (RoundManager.playerCreatedGame?) RoundManager.gameStatus!!.playerStates.second.coins else RoundManager.gameStatus!!.playerStates.first.coins
    private val coinsText = FontObject(coins.toString(), 60, Vector2(defaultPosition.x - 150f, Application.HEIGHT - 135f))

    private val enemyFireTower = RoundManager.gameStatus!!.availableTowers.fireTower
    private val fireCard =  SmartObject("Cards/LabelTower/${getUnitName(enemyFireTower.type, enemyFireTower.level)}.png", Vector2(defaultPosition.x - 80f, Application.HEIGHT - 173f), 1f)
    private val fireDamageNumber = FontObject(enemyFireTower.damage.toString(), 25, Vector2(defaultPosition.x - 55f, Application.HEIGHT - 153f))
    private val fireRangeNumber = FontObject(enemyFireTower.range.toString(), 25, Vector2(defaultPosition.x - 23f, Application.HEIGHT - 153f))

    private val enemyWaterTower = RoundManager.gameStatus!!.availableTowers.waterTower
    private val waterCard =  SmartObject("Cards/LabelTower/${getUnitName(enemyWaterTower.type, enemyWaterTower.level)}.png", Vector2(defaultPosition.x - 170f, Application.HEIGHT - 303f), 1f)
    private val waterDamageNumber = FontObject(enemyWaterTower.damage.toString(), 25, Vector2(defaultPosition.x - 145f, Application.HEIGHT - 283f))
    private val waterRangeNumber = FontObject(enemyWaterTower.range.toString(), 25, Vector2(defaultPosition.x - 113f, Application.HEIGHT - 283f))

    private val enemyGrassTower = RoundManager.gameStatus!!.availableTowers.grassTower
    private val grassCard =  SmartObject("Cards/LabelTower/${getUnitName(enemyGrassTower.type, enemyGrassTower.level)}.png", Vector2(defaultPosition.x - 80f, Application.HEIGHT - 303f), 1f)
    private val grassDamageNumber = FontObject(enemyGrassTower.damage.toString(), 25, Vector2(defaultPosition.x - 55f, Application.HEIGHT - 283f))
    private val grassRangeNumber = FontObject(enemyGrassTower.range.toString(), 25, Vector2(defaultPosition.x - 23f, Application.HEIGHT - 283f))

    init {
        expandButton.onClick = { expand() }
        collapseButton.onClick = { collapse() }

        collapseButton.hide = true

        coinsText.zIndex = 2
        panel.zIndex = 2
        expandButton.zIndex = 2
        collapseButton.zIndex = 2
        fireCard.zIndex = 2
        fireDamageNumber.zIndex = 2
        fireRangeNumber.zIndex = 2
        waterCard.zIndex = 2
        waterDamageNumber.zIndex = 2
        waterRangeNumber.zIndex = 2
        grassCard.zIndex = 2
        grassDamageNumber.zIndex = 2
        grassRangeNumber.zIndex = 2

        hideUI(true)
    }

    private fun expand() {
        collapseButton.hide = false
        expandButton.hide = true
        panel.setPosition(Vector2(defaultPosition.x - 180f, defaultPosition.y))
        hideUI(false)
    }

    private fun collapse() {
        collapseButton.hide = true
        expandButton.hide = false
        panel.setPosition(defaultPosition)
        hideUI(true)
    }

    private fun hideUI(shouldHide: Boolean) {
        coinsText.hide = shouldHide
        fireCard.hide = shouldHide
        fireDamageNumber.hide = shouldHide
        fireRangeNumber.hide = shouldHide
        waterCard.hide = shouldHide
        waterDamageNumber.hide = shouldHide
        waterRangeNumber.hide = shouldHide
        grassCard.hide = shouldHide
        grassDamageNumber.hide = shouldHide
        grassRangeNumber.hide = shouldHide

    }

    fun die() {
        panel.die()
        expandButton.die()
        collapseButton.die()

        fireCard.die()
        fireDamageNumber.die()
        fireRangeNumber.die()

        waterCard.die()
        waterDamageNumber.die()
        waterRangeNumber.die()

        grassCard.die()
        grassDamageNumber.die()
        grassRangeNumber.die()

        coinsText.die()
    }

}