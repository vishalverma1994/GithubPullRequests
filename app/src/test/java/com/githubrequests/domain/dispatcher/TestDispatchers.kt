package com.githubrequests.domain.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher

@ExperimentalCoroutinesApi
class TestDispatchers : CoroutineDispatcherProvider {
    private val dispatcher = StandardTestDispatcher()
    override val main: CoroutineDispatcher
        get() = dispatcher
    override val mainImmediate: CoroutineDispatcher
        get() = dispatcher
    override val io: CoroutineDispatcher
        get() = dispatcher
    override val default: CoroutineDispatcher
        get() = dispatcher
    override val unconfirmed: CoroutineDispatcher
        get() = dispatcher

}