package com.githubrequests.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githubrequests.domain.dispatcher.CoroutineDispatcherProvider
import com.githubrequests.domain.usecases.FetchGithubRequestsUseCase
import com.githubrequests.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubRequestsViewModel @Inject constructor(
    private val fetchGithubRequestsUseCase: FetchGithubRequestsUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : ViewModel() {

    // for handling errors in ui
    private val errorChannel = Channel<Failure>()
    val errorEventFlow = errorChannel.receiveAsFlow()

    // flow for the all currencies supported
    private val successFlow = MutableStateFlow<Success>(Success.Idle)
    val successEventFlow = successFlow.asStateFlow()

    //fetch github requests from the network
    fun fetchGithubRequests() {
        viewModelScope.launch(dispatcher.mainImmediate) {
            fetchGithubRequestsUseCase.invoke(OWNER, REPO, CLOSED).flowOn(dispatcher.io).collect { response ->
                handleResponse(response) {
                    successFlow.emit(it)
                }
            }
        }
    }

    private suspend fun handleResponse(
        result: Either<Failure, Success>,
        onSuccess: suspend (Success) -> Unit
    ) {
        when (result) {
            is Either.Left -> errorChannel.send(result.data)
            is Either.Right -> onSuccess(result.data)
        }
    }

}