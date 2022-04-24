package com.cows.game.hud

import com.badlogic.gdx.math.Vector2
import com.cows.game.Application
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
    private var coins = 10
    private var coinsText = FontObject(coins.toString(), 60, Vector2(this.position.x+68f, 500f))

    // FIRE TOWER🔥
    private val fireTowerData = Redux.jsonAvailableUnits!!.fireUnit
    val fireTowerBackground = SmartObject("HUD/banner-fire.png", Vector2(this.position.x+32f, 323f), 1f)
    val fireHealthNumber = FontObject(fireTowerData.health.toString(), 25, Vector2(this.position.x + 70f, 439f))
    val fireMovementSpeedNumber = FontObject(fireTowerData.movementSpeed.toString(), 25, Vector2(this.position.x + 135f, 439f))
    val fireTowerButton = Button("Cards/"+getUnitName(UnitType.FIRE, fireTowerData.level)+".png", Vector2(this.position.x, 330f))

    // GRASS TOWER🌿
    private val grassTowerData = Redux.jsonAvailableUnits!!.grassUnit
    val grassTowerBackground = SmartObject("HUD/banner-grass.png", Vector2(this.position.x+32f , 199f), 1f)
    val grassHealthNumber = FontObject(grassTowerData.health.toString(), 25, Vector2(this.position.x + 70f, 315f))
    val grassMovementSpeedNumber = FontObject(grassTowerData.movementSpeed.toString(), 25, Vector2(this.position.x + 135f, 315f))
    val waterTowerButton = Button("Cards/"+getUnitName(UnitType.GRASS, grassTowerData.level)+".png", Vector2(this.position.x , 206f))

    // WATER TOWER💧
    private val waterTowerData = Redux.jsonAvailableUnits!!.waterUnit
    val waterTowerBackground = SmartObject("HUD/banner-water.png", Vector2(this.position.x+32f, 75f), 1f)
    val waterHealthNumber = FontObject(waterTowerData.health.toString(), 25, Vector2(this.position.x + 70f, 192f))
    val waterMovementSpeedNumber = FontObject(waterTowerData.movementSpeed.toString(), 25, Vector2(this.position.x + 135f, 192f))
    val grassTowerButton = Button("Cards/"+getUnitName(UnitType.WATER, waterTowerData.level)+".png", Vector2(this.position.x, 83f))


    init {
        fireTowerButton.position.x += ActionPanel.WIDTH/2 - fireTowerButton.texture.width/2
        waterTowerButton.position.x += ActionPanel.WIDTH/2 - waterTowerButton.texture.width/2
        grassTowerButton.position.x += ActionPanel.WIDTH/2 - grassTowerButton.texture.width/2
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
        coinsText.text = (++ coins).toString()
    }

    fun spawnTower(type: UnitType) {
        if (coins>0 && lastTile != null ) {
            coinsText.text = (-- coins).toString()
            towerToBeSpawned = type
            val reduxTowerModel = Redux.jsonAvailableTowers!!.getTower(type)
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
        if(coins <= 0){
            hideUI(true)
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
        fireTowerBackground.hide = hide
        grassTowerBackground.hide = hide
        waterTowerBackground.hide = hide
        fireHealthNumber.hide = hide
        fireMovementSpeedNumber.hide = hide
        grassHealthNumber.hide = hide
        grassMovementSpeedNumber.hide = hide
        waterHealthNumber.hide = hide
        waterMovementSpeedNumber.hide = hide
    }
}