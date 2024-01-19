package com.example.reader.screens.login

import androidx.work.Operation.State.SUCCESS

data class LoadingState(val status: Status, val message: String? = null) {

    companion object{
        val IDLE = LoadingState(Status.IDLE)
        val SUCCESS = LoadingState(Status.SUCCESS)
        val FAILED = LoadingState(Status.FAILED)
        val LOADING = LoadingState(Status.LOADING)
    }

    enum class Status {
        LOADING,
        SUCCESS,
        FAILED,
        IDLE,
    }

}
