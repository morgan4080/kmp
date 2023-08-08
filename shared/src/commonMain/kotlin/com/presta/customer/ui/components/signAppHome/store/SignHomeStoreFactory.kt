package com.presta.customer.ui.components.signAppHome.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.presta.customer.network.signHome.data.SignHomeRepository
import com.presta.customer.network.signHome.model.PrestaSignUserDetailsResponse
import com.presta.customer.prestaDispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SignHomeStoreFactory(
    private val storeFactory: StoreFactory

) : KoinComponent {
    private val signHomeRepository by inject<SignHomeRepository>()

    fun create(): SignHomeStore =
        object : SignHomeStore,
            Store<SignHomeStore.Intent, SignHomeStore.State, Nothing> by storeFactory.create(
                name = "SignHomeStore",
                initialState = SignHomeStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed class Msg {
        data class SignHomeLoading(val isLoading: Boolean = true) : Msg()
        data class SignTenantByPhoneNumberLoaded(val signTenantById: PrestaSignUserDetailsResponse) :
            Msg()

        data class SignTenantByMemberNumberLoaded(val signTenantByMemberNumber: PrestaSignUserDetailsResponse) :
            Msg()

        data class UpdateSignTenantDetails(val updateSignTenantDetails: PrestaSignUserDetailsResponse) :
            Msg()
        data class UpdateSignTenanPersonalInfo(val updateSignTenantPersonalInfo: PrestaSignUserDetailsResponse) :
            Msg()

        data class SignHomeFailed(val error: String?) : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<SignHomeStore.Intent, Unit, SignHomeStore.State, SignHomeStoreFactory.Msg, Nothing>(
            prestaDispatchers.main
        ) {
        override fun executeAction(action: Unit, getState: () -> SignHomeStore.State) {

        }

        override fun executeIntent(
            intent: SignHomeStore.Intent, getState: () -> SignHomeStore.State
        ): Unit =
            when (intent) {

                is SignHomeStore.Intent.GetPrestaTenantByPhoneNumber -> getPrestaTenantByPhoneNumber(
                    token = intent.token,
                    phoneNumber = intent.phoneNumber
                )

                is SignHomeStore.Intent.GetPrestaTenantByMemberNumber -> getPrestaTenantByMemberNumber(
                    token = intent.token,
                    memberNumber = intent.memberNumber
                )

                is SignHomeStore.Intent.UpdatePrestaTenantDetails -> updatePrestaMemberDetails(
                    token = intent.token,
                    memberRefId = intent.memberRefId,
                    details = intent.details
                )
                is SignHomeStore.Intent.UpdatePrestaTenantPersonalInfo->updatePrestaMemberPersonalInfo(
                    token = intent.token,
                    memberRefId = intent.memberRefId,
                    firstName = intent.firstName,
                    lastName = intent.lastName,
                    phoneNumber = intent.phoneNumber,
                    idNumber = intent.idNumber,
                    email = intent.email
                )

            }

        private var getPrestaTenantByPhoneNumberJob: Job? = null

        private fun getPrestaTenantByPhoneNumber(
            token: String,
            phoneNumber: String
        ) {
            if (getPrestaTenantByPhoneNumberJob?.isActive == true) return

            dispatch(Msg.SignHomeLoading())

            getPrestaTenantByPhoneNumberJob = scope.launch {
                signHomeRepository.getTenantByPhoneNumber(
                    token = token,
                    phoneNumber = phoneNumber
                ).onSuccess { response ->
                    dispatch(Msg.SignTenantByPhoneNumberLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.SignHomeFailed(e.message))
                }

                dispatch(Msg.SignHomeLoading(false))
            }
        }

        private var getPrestaTenantByMemberNumberJob: Job? = null
        private fun getPrestaTenantByMemberNumber(
            token: String,
            memberNumber: String
        ) {
            if (getPrestaTenantByMemberNumberJob?.isActive == true) return

            dispatch(Msg.SignHomeLoading())

            getPrestaTenantByMemberNumberJob = scope.launch {
                signHomeRepository.getTenantByMemberNumber(
                    token = token,
                    memberNumber = memberNumber
                ).onSuccess { response ->
                    dispatch(Msg.SignTenantByMemberNumberLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.SignHomeFailed(e.message))
                }
                dispatch(Msg.SignHomeLoading(false))
            }
        }

        private var updatePrestaMemberDetailsJob: Job? = null
        private fun updatePrestaMemberDetails(
            token: String,
            memberRefId: String,
            details: MutableMap<String, String>
        ) {
            if (updatePrestaMemberDetailsJob?.isActive == true) return

            dispatch(Msg.SignHomeLoading())

            updatePrestaMemberDetailsJob = scope.launch {
                signHomeRepository.upDateMemberDetails(
                    token = token,
                    memberRefId = memberRefId,
                    details = details
                ).onSuccess { response ->
                    dispatch(Msg.UpdateSignTenantDetails(response))
                }.onFailure { e ->
                    dispatch(Msg.SignHomeFailed(e.message))
                }
                dispatch(Msg.SignHomeLoading(false))
            }
        }

        private var updatePrestaMemberPersonalInfoJob: Job? = null
        private fun updatePrestaMemberPersonalInfo(
            token: String,
            memberRefId: String,
            firstName: String,
            lastName: String,
            phoneNumber: String,
            idNumber: String,
            email: String,
        ) {
            if (updatePrestaMemberPersonalInfoJob?.isActive == true) return

            dispatch(Msg.SignHomeLoading())

            updatePrestaMemberPersonalInfoJob = scope.launch {
                signHomeRepository.upDateMemberPersonalInfo(
                    token = token,
                    memberRefId = memberRefId,
                    firstName=firstName,
                    lastName= lastName,
                    phoneNumber=phoneNumber,
                    idNumber=idNumber,
                    email=email
                ).onSuccess { response ->
                    dispatch(Msg.UpdateSignTenantDetails(response))
                }.onFailure { e ->
                    dispatch(Msg.SignHomeFailed(e.message))
                }
                dispatch(Msg.SignHomeLoading(false))
            }
        }
    }
    private object ReducerImpl :
        Reducer<SignHomeStore.State, Msg> {
        override fun SignHomeStore.State.reduce(msg: Msg): SignHomeStore.State =
            when (msg) {
                is Msg.SignHomeLoading -> copy(isLoading = msg.isLoading)
                is Msg.SignHomeFailed -> copy(error = msg.error)
                is Msg.SignTenantByPhoneNumberLoaded -> copy(prestaTenantByPhoneNumber = msg.signTenantById)
                is Msg.SignTenantByMemberNumberLoaded -> copy(prestaTenantByMemberNumber = msg.signTenantByMemberNumber)
                is Msg.UpdateSignTenantDetails -> copy(updatePrestaTenantDetails = msg.updateSignTenantDetails)
                is Msg.UpdateSignTenanPersonalInfo-> copy(updatePrestaTenantPersonalInfo=msg.updateSignTenantPersonalInfo)

            }
    }


}