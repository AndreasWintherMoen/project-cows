package com.cows.game

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class CreateGameScope(override val coroutineContext: CoroutineContext = Job()) : CoroutineScope {}