package com.audcode.data.exceptions

sealed class Failure {
    object NetworkConnection : Failure()
    data class ServerError(val message : String) : Failure()
    data class UnExpectedError( val message : String) : Failure()
}