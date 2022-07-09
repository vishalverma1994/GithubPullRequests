package com.githubrequests.utils

import androidx.annotation.NonNull

// helper class to handle the success and failures of the app
sealed class Either<out L, out R> {
    data class Left<out L>(@NonNull val data: L) : Either<L, Nothing>()
    data class Right<out R>(@NonNull val data: R) : Either<Nothing, R>()
}

sealed class Success {
    abstract class ViewState : Success()

    object Idle : Success()
    object Loading : Success()
}

sealed class Failure {
    data class ServerError(val error: String) : Failure()
    object NetworkConnectionError : Failure()
    data class GenericError(val exception: Exception) : Failure()
    abstract class FeatureSpecificError : Failure()
}