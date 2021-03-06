package com.cows.game.hud

import com.badlogic.gdx.math.Vector2
import com.cows.game.ClickSubscriber
import com.cows.game.Redux
import com.cows.game.controllers.PlanningTowerController
import com.cows.game.controllers.TileController
import com.cows.game.enums.GameState
import com.cows.game.enums.TileType
import com.cows.game.enums.UnitType
import com.cows.game.managers.GameStateManager
import com.cows.game.managers.RoundManager
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
    private var lastTile: TileController? = null
    private var selectedTowerRadius: SmartObject? = null
    private var lastOccupiedTile: TileController? = null;
    private var coins = UnitCounterPanel.calculateAvailableUnits()
    private var coinsText = FontObject(coins.toString(), 60, Vector2(this.position.x+68f, 500f))

    private val costPerDefenseTower = 3


    // FIRE TOWER🔥
    private val fireTowerData = RoundManager.gameStatus!!.availableTowers.fireTower
    val fireTowerBackground = SmartObject("Cards/banner-fire-defence.png", Vector2(this.position.x+32f, 323f), 1f)
    val fireDamageNumber = FontObject(fireTowerData.damage.toString(), 25, Vector2(this.position.x + 70f, 439f))
    val fireRangeNumber = FontObject(fireTowerData.range.toString(), 25, Vector2(this.position.x + 135f, 439f))
    val fireTowerButton = Button("Cards/"+getUnitName(UnitType.FIRE, fireTowerData.level)+".png", Vector2(this.position.x, 330f))

    // GRASS TOWER🌿
    private val grassTowerData = RoundManager.gameStatus!!.availableTowers.grassTower
    val grassTowerBackground = SmartObject("Cards/banner-grass-defence.png", Vector2(this.position.x+32f , 199f), 1f)
    val grassDamageNumber = FontObject(grassTowerData.damage.toString(), 25, Vector2(this.position.x + 70f, 315f))
    val grassRangeNumber = FontObject(grassTowerData.range.toString(), 25, Vector2(this.position.x + 135f, 315f))
    val grassTowerButton = Button("Cards/"+getUnitName(UnitType.GRASS, grassTowerData.level)+".png", Vector2(this.position.x, 206f))

    // WATER TOWER💧
    private val waterTowerData = RoundManager.gameStatus!!.availableTowers.waterTower
    val waterTowerBackground = SmartObject("Cards/banner-water-defence.png", Vector2(this.position.x+32f, 75f), 1f)
    val waterDamageNumber = FontObject(waterTowerData.damage.toString(), 25, Vector2(this.position.x + 70f, 192f))
    val waterRangeNumber = FontObject(waterTowerData.range.toString(), 25, Vector2(this.position.x + 135f, 192f))
    val waterTowerButton = Button("Cards/"+getUnitName(UnitType.WATER, waterTowerData.level)+".png", Vector2(this.position.x , 83f))


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

        coinsText.zIndex = 3
        removeSelectedTower.zIndex = 3
        selectTileText.zIndex = 3

        fireTowerBackground.zIndex = 3
        fireDamageNumber.zIndex = 3
        fireRangeNumber.zIndex = 3
        fireTowerButton.zIndex = 3

        grassTowerBackground.zIndex = 3
        grassDamageNumber.zIndex = 3
        grassRangeNumber.zIndex = 3
        grassTowerButton.zIndex = 3

        waterTowerBackground.zIndex = 3
        waterDamageNumber.zIndex = 3
        waterRangeNumber.zIndex = 3
        waterTowerButton.zIndex = 3
    }

    private fun removeTower() {
        if (GameStateManager.currentGameState != GameState.PLANNING_DEFENSE) return
        removeSelectedTower.hide = true
        val tower = spawnedTowers.first { tower -> tower.model.tileCoordinate == lastTile!!.tileModel.coordinate }
        spawnedTowers.remove(tower)
        tower.view.die()
        coins += costPerDefenseTower
        coinsText.text = coins.toString()
    }

    fun spawnTower(type: UnitType) {
        if (GameStateManager.currentGameState != GameState.PLANNING_DEFENSE) return
        if (coins>costPerDefenseTower && lastTile != null ) {
            coins -= costPerDefenseTower
            coinsText.text = coins.toString()
            towerToBeSpawned = type
            hideUI(true)
            val reduxTowerModel = RoundManager.gameStatus?.availableTowers?.getTower(type)
            reduxTowerModel?.let {
                val towerModel = TowerModel(towerToBeSpawned, it.level, lastTile!!.tileModel.coordinate, it.range!!, it.damage!!)
                val towerController = PlanningTowerController(towerModel)
                spawnedTowers.add(towerController)
            }
        } else {
            //TODO: Give the user some feedback that they don't have any units left
        }
    }

    private fun onStartButtonClicked() {
        if (GameStateManager.currentGameState != GameState.PLANNING_DEFENSE) return
        readyButton.hide = true
        waitingButton.hide = false
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
        if (GameStateManager.currentGameState != GameState.PLANNING_DEFENSE) return
        selectTileText.hide = true
        lastTile = tile
        tile.tileView.showHighlight = true
        hideUI(false)
    }

    fun clickOccupiedTile(tile:TileController){
        if (GameStateManager.currentGameState != GameState.PLANNING_DEFENSE) return
        selectTileText.hide = true
        lastTile = tile
        tile.tileView.showHighlight = true
        val clickedTower = getTowerAtTile(tile)
        selectedTowerRadius = SmartObject("Towers/attackRadius.png", tile.tileModel.coordinate.toVector2(), 0.25f + clickedTower!!.model.range * 0.5f)
        selectedTowerRadius!!.zIndex = 9
        selectedTowerRadius!!.sprite.setPosition(tile.tileModel.coordinate.toVector2().x - selectedTowerRadius!!.sprite.width/2 + TileModel.WIDTH/2, tile.tileModel.coordinate.toVector2().y - selectedTowerRadius!!.sprite.height/2 + TileModel.HEIGHT/2)
        removeSelectedTower.hide = false
        readyButton.hide = true
        hideUI(true)
    }

    override fun click(position: Vector2, tile: TileController?) {
        if (GameStateManager.currentGameState != GameState.PLANNING_DEFENSE) return
        selectTileText.hide = false
        readyButton.hide = false
        lastTile?.let { it.tileView.showHighlight = false }
        lastOccupiedTile?.let { it.tileView.showHighlight = false }
        selectedTowerRadius?.let { it.die() }
        if(coins < costPerDefenseTower){
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
        } else {
            selectedTowerRadius?.die()
            selectedTowerRadius = null
            removeSelectedTower.hide = true
            readyButton.hide = false
        }
        clickTile(tile)
    }

    private fun checkIfTileIsOccupied(position: Coordinate): Boolean {
        return spawnedTowers.any { it.model.tileCoordinate == position }
    }

    private fun getTowerAtTile(tile: TileController): PlanningTowerController? {
        return spawnedTowers.find { it.model.tileCoordinate == tile.tileModel.coordinate }
    }

    override fun die() {
        super.die()
        removeSelectedTower.die()
        spawnedTowers.forEach { it.view.die() }
        spawnedTowers.clear()
        towerToBeSpawned = UnitType.NONE
        fireDamageNumber.die()
        fireRangeNumber.die()
        fireTowerBackground.die()
        grassDamageNumber.die()
        grassRangeNumber.die()
        grassTowerBackground.die()
        waterDamageNumber.die()
        waterRangeNumber.die()
        waterTowerBackground.die()
    }

    override fun hideUI(hide: Boolean) {
        fireDamageNumber.hide = hide
        fireRangeNumber.hide = hide
        fireTowerBackground.hide = hide
        fireTowerButton.hide = hide
        grassDamageNumber.hide = hide
        grassRangeNumber.hide = hide
        grassTowerBackground.hide = hide
        grassTowerButton.hide = hide
        waterDamageNumber.hide = hide
        waterRangeNumber.hide = hide
        waterTowerBackground.hide = hide
        waterTowerButton.hide = hide
    }
}