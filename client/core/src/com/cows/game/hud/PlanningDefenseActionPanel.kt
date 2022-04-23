package com.cows.game.hud

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.cows.game.Redux
import com.cows.game.enums.UnitType
import com.cows.game.managers.TowerSpawner
import com.cows.game.roundSimulation.rawJsonData.JsonTower
import com.cows.game.serverConnection.ServerConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PlanningDefenseActionPanel(): PlanningActionPanel() {
    private val cancelPlacementButton = Button("HUD/cancel-button.png", Vector2(Gdx.graphics.width - 100f, 30f)) { cancelPlacement() }


    init {
        cancelPlacementButton.hide = true
        fireTowerButton.onClick =  { selectTower(UnitType.FIRE)}
        waterTowerButton.onClick = { selectTower(UnitType.WATER) }
        grassTowerButton.onClick = { selectTower(UnitType.GRASS) }
//        startGameButton.onClick = { onStartGame.invoke() }
        startGameButton.onClick = { onStartButtonClicked() }
    }

    private fun cancelPlacement() {
        TowerSpawner.cancelPlacement()
        cancelPlacementButton.hide = true
    }

    private fun confirmPlacement() {
        TowerSpawner.cancelPlacement()
        cancelPlacementButton.hide = true
    }

    private fun selectTower(type: UnitType) {
        if (unitCounterPanel.hasAvailableUnits()) {
            TowerSpawner.selectTower(type) { confirmPlacement(); unitCounterPanel.addUnit(type) }
            cancelPlacementButton.hide = false
        } else {
            //TODO: Give the user some feedback that they don't have any units left
        }
    }

    private fun onStartButtonClicked() {
        println("PlanningDefenseActionPanel::onStartButtonClicked")
        val towers = TowerSpawner
            .spawnedTowers
            .map { it.model }
            .mapIndexed { index, tower -> JsonTower(index, tower.type, tower.tileCoordinate, 5f)}
        GlobalScope.launch(Dispatchers.IO) {
            val roundSimulation = ServerConnection.sendDefendInstructions(towers)
            println("Received sound simulation $roundSimulation")
            Redux.jsonRoundSimulation = roundSimulation
        }
    }

    override fun die() {
        super.die()
        cancelPlacementButton.die()
    }
}