package com.presta.customer.ui.components.auth.poller

import com.presta.customer.network.authDevice.data.AuthRepository
import com.presta.customer.network.authDevice.model.RefreshTokenResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn

class AuthPoller(
    private val authRepository: AuthRepository,
    private val dispatcher: CoroutineDispatcher
): AuthPollerInterface {
    @OptIn(DelicateCoroutinesApi::class)
    override fun poll(delay: Long, tenantId: String, refId: String): Flow<Result<RefreshTokenResponse>> {
        return channelFlow {
            while (!isClosedForSend) {
                val data = authRepository.updateAuthToken(tenantId, refId)
                send(data)
                delay(delay)
            }
        }.flowOn(dispatcher)
    }

    override fun close() {
        dispatcher.cancel()
    }
}