package com.cows.game.hud

import com.badlogic.gdx.math.Vector2
import com.cows.game.ClickSubscriber
import com.cows.game.Redux
import com.cows.game.controllers.PlanningTowerController
import com.cows.game.controllers.TileController
import com.cows.game.enums.TileType
import com.cows.game.enums.UnitType
import com.cows.game.map.Coordinate
import com.cows.game.models.TileModel
import com.cows.game.models.TowerModel
import com.cows.game.roundSimulation.rawJsonData.JsonTower
import com.cows.game.serverConnection.ServerConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PlanningDefenseActionPanel(): PlanningActionPanel(), ClickSubscriber {
    private val removeSelectedTower = Button("Buttons/remove-btn.png", Vector2(this.position.x, 0f)) { removeTower() }
    private val selectTileText = SmartObject("HUD/SelectTileText.png", Vector2(this.position.x, 201f), 1f)
    private var towerToBeSpawned = UnitType.NONE
    val spawnedTowers = mutableListOf<PlanningTowerController>()
    private var onSpawnTower: (() -> Unit)? = null
    private var lastTile: TileController? = null
    private var selectedTowerRadius: SmartObject? = null
    private var lastOccupiedTile: TileController? = null;

    init {
        selectTileText.hide = false
        removeSelectedTower.hide = true
        fireTowerButton.onClick =  { spawnTower(UnitType.FIRE)}
        waterTowerButton.onClick = { spawnTower(UnitType.WATER) }
        grassTowerButton.onClick = { spawnTower(UnitType.GRASS) }
        readyButton.onClick = { onStartButtonClicked() }
        subscribeToClickEvents()
        hideUI(true)
    }

    private fun removeTower() {
        removeSelectedTower.hide = true
        val tower = spawnedTowers.first { tower -> tower.model.tileCoordinate == lastTile!!.tileModel.coordinate }
        spawnedTowers.remove(tower)
        tower.view.die()
        unitCounterPanel.removeUnit(tower.model.type)
    }

    fun spawnTower(type: UnitType) {
        if (unitCounterPanel.hasAvailableUnits() && lastTile != null ) {
            unitCounterPanel.addUnit(type)
            towerToBeSpawned = type
            val reduxTowerModel = Redux.gameStatus!!.availableTowers.getTower(type)
            val towerModel = TowerModel(towerToBeSpawned, reduxTowerModel.level, lastTile!!.tileModel.coordinate, reduxTowerModel.range!!, reduxTowerModel.damage!!)
            val towerController = PlanningTowerController(towerModel)
            spawnedTowers.add(towerController)
            onSpawnTower?.invoke()
            hideUI(true)
        } else {
            //TODO: Give the user some feedback that they don't have any units left
        }
    }

    private fun onStartButtonClicked() {
        val towers = spawnedTowers
            .map { it.model }
            .map { JsonTower(null, it.type, it.level, it.tileCoordinate, null, null, null)}
        GlobalScope.launch(Dispatchers.IO) {
            val roundSimulation = ServerConnection.sendDefendInstructions(towers)
            println("Received sound simulation $roundSimulation")
            Redux.jsonRoundSimulation = roundSimulation
        }
    }

    fun clickTile(tile:TileController){
        selectTileText.hide = true
        lastTile = tile
        tile.tileView.showHighlight = true
        hideUI(false)
    }

    fun clickOccupiedTile(tile:TileController){
        selectTileText.hide = true
        lastTile = tile
        tile.tileView.showHighlight = true
        selectedTowerRadius = SmartObject("Towers/attackRadius.png", tile.tileModel.coordinate.toVector2(), 1f)
        selectedTowerRadius!!.sprite.setPosition(tile.tileModel.coordinate.toVector2().x - selectedTowerRadius!!.sprite.width/2 + TileModel.WIDTH/2, tile.tileModel.coordinate.toVector2().y - selectedTowerRadius!!.sprite.height/2 + TileModel.HEIGHT/2)
        removeSelectedTower.hide = false
        readyButton.hide = true
        hideUI(true)
    }

    override fun click(position: Vector2, tile: TileController?) {
        selectTileText.hide = false
        readyButton.hide = false
        lastTile?.let { it.tileView.showHighlight = false }
        lastOccupiedTile?.let { it.tileView.showHighlight = false }
        selectedTowerRadius?.let { it.die() }
        if(!unitCounterPanel.hasAvailableUnits()){
            hideUI(true)
            return
        }
        if (tile == null){
            hideUI(true)
            return
        }
        if (tile.tileModel.type == TileType.PATH){
            hideUI(true)
            return
        }
        if (checkIfTileIsOccupied(tile.tileModel.coordinate)){
            clickOccupiedTile(tile)
            return
        }
        clickTile(tile)
    }

    private fun checkIfTileIsOccupied(position: Coordinate): Boolean {
        return spawnedTowers.any { it.model.tileCoordinate == position }
    }

    override fun die() {
        super.die()
        removeSelectedTower.die()
        spawnedTowers.forEach { it.view.die() }
        spawnedTowers.clear()
        towerToBeSpawned = UnitType.NONE
    }

    override fun hideUI(hide: Boolean) {
        fireTowerButton.hide = hide
        waterTowerButton.hide = hide
        grassTowerButton.hide = hide
        unitCounterPanel.hideUnits = hide
    }
}