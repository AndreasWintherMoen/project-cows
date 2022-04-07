package com.cows.game.managers

import com.badlogic.gdx.math.Vector2
import com.cows.game.ClickSubscriber
import com.cows.game.controllers.PlanningTowerController
import com.cows.game.controllers.TileController
import com.cows.game.enums.GameState
import com.cows.game.enums.TileType
import com.cows.game.enums.TowerType
import com.cows.game.gameState.GameStateSubscriber
import com.cows.game.map.Coordinate
import com.cows.game.models.TowerModel

object TowerSpawner: GameStateSubscriber(), ClickSubscriber {
    private var towerToBeSpawned = TowerType.NONE
    private val spawnedTowers = mutableListOf<PlanningTowerController>()
    private var onSpawnTower: (() -> Unit)? = null

    fun selectTower(type: TowerType, onSpawnTower: () -> Unit) {
        towerToBeSpawned = type
        subscribeToClickEvents()
        this.onSpawnTower = onSpawnTower
    }

    fun cancelPlacement() {
        towerToBeSpawned = TowerType.NONE
        unsubscribeToClickEvents()
    }

    fun spawnActiveTower(tileCoordinate: Coordinate) {
        val towerModel = TowerModel(towerToBeSpawned, tileCoordinate)
        val towerController = PlanningTowerController(towerModel)
        spawnedTowers.add(towerController)
        onSpawnTower?.invoke()
    }

    override fun onChangeGameState(oldGameState: GameState, newGameState: GameState) {
        if (oldGameState == GameState.PLANNING_DEFENSE && newGameState == GameState.ACTIVE_GAME) {
            // TODO: serialize spawnedTowers and send info to API module
            spawnedTowers.forEach { it.view.die() }
            spawnedTowers.clear()
            towerToBeSpawned = TowerType.NONE
        }
    }

    override fun click(position: Vector2, tile: TileController?) {
        if (tile == null) return
        if (tile.tileModel.type == TileType.PATH) return
        if (checkIfTileIsOccupied(tile.tileModel.coordinate)) return
        spawnActiveTower(tile.tileModel.coordinate)
    }

    private fun checkIfTileIsOccupied(position: Coordinate): Boolean {
        return spawnedTowers.any { it.model.tileCoordinate == position }
    }

}