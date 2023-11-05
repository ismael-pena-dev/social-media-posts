package com.pena.ismael.socialmediapager.core.services.connectivityobserver

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<Status>

    fun isConnectionAvailable(): Boolean

    sealed interface Status {
        data object Available: Status
        data object Unavailable: Status
        data object Losing: Status
        data object Lost: Status
    }
}

