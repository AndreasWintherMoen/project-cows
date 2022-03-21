package com.cows.game.managers

object FunctionDelayer {
    private val thisFrameFunctions = mutableListOf<() -> Unit>()
    private val nextFrameFunctions = mutableListOf<() -> Unit>()

    fun invokeFunctionAtEndOfThisFrame(function: () -> Unit) = thisFrameFunctions.add(function)

    fun invokeFunctionAtEndOfNextFrame(function: () -> Unit) = nextFrameFunctions.add(function)

    fun invokeRegisteredFunctions() {
        thisFrameFunctions.forEach { it.invoke() }
        thisFrameFunctions.clear()
        thisFrameFunctions += nextFrameFunctions
        nextFrameFunctions.clear()
    }
}