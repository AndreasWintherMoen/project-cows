package com.cows.game.enums

// The different states the game can be in. The game always start with START_MENU
enum class GameState {
    NONE,
    START_MENU,
    PLANNING_DEFENSE,
    PLANNING_ATTACK,
    ACTIVE_GAME
}