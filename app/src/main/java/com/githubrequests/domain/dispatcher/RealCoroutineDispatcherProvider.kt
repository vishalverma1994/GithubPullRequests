package com.githubrequests.domain.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * provide the real coroutine dispatchers
 */
open class RealCoroutineDispatcherProvider : CoroutineDispatcherProvider {
    override val main: CoroutineDispatcher by lazy { Dispatchers.Main}
    override val mainImmediate: CoroutineDispatcher  by lazy { Dispatchers.Main.immediate}
    override val io: CoroutineDispatcher by lazy { Dispatchers.IO }
    override val default: CoroutineDispatcher by lazy { Dispatchers.Default }
    override val unconfirmed: CoroutineDispatcher by lazy { Dispatchers.Unconfined }
}
