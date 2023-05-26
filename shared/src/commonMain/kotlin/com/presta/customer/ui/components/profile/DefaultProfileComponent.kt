package com.presta.customer.ui.components.profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.auth.store.AuthStoreFactory
import com.presta.customer.ui.components.profile.store.ProfileStore
import com.presta.customer.ui.components.profile.store.ProfileStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultProfileComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val onProfileClicked: () -> Unit
) : ProfileComponent, ComponentContext by componentContext {

    override val authStore =
        instanceKeeper.getStore {
            AuthStoreFactory(
                storeFactory = storeFactory,
                phoneNumber = null,
                isTermsAccepted = false,
                isActive = false
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val authState: StateFlow<AuthStore.State> = authStore.stateFlow

    override val profileStore =
        instanceKeeper.getStore {
            ProfileStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val profileState: StateFlow<ProfileStore.State> = profileStore.stateFlow

    override fun onAuthEvent(event: AuthStore.Intent) {
        authStore.accept(event)
    }

    override fun onEvent(event: ProfileStore.Intent) {
        profileStore.accept(event)
    }

    init {
        onEvent(ProfileStore.Intent.GetBalances(
            token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ3SVFSTGZNLU1fYUNEX1BLSW56Z2haTFB6MDF5NDA1WEJkS0lJRGxFaWdjIn0.eyJleHAiOjE2ODUwNzEyODUsImlhdCI6MTY4NTA3MDk4NSwianRpIjoiN2I1NzJkMzctMTAwNS00ODVmLTkyNWUtZmQ5MzNiN2VlZWJjIiwiaXNzIjoiaHR0cHM6Ly9zdGFnaW5nLWlhbS5wcmVzdGEuY28ua2UvYXV0aC9yZWFsbXMvdDEwMDA3IiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImJmMGRlYzg2LWQ3YmUtNDMyZi1iNzA3LTI3Njc2OThlMTNmNCIsInR5cCI6IkJlYXJlciIsImF6cCI6ImxvYW4tYXBwbGljYXRpb25zLXNlcnZpY2UiLCJzZXNzaW9uX3N0YXRlIjoiNTNhMTkzYmItM2U1OS00NmZlLWE3YmUtM2Q4MTA5ZjEwMmNjIiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJ0ZW5hbnRJZCI6InQxMDAwNyIsIm5hbWUiOiJNT1JHQU4gTVVUVUdJIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiY2RiMWI4NjMtMTUyZi00NTdlLWEwZDktZDM1ZWRjNzU3ZTc3IiwiZ2l2ZW5fbmFtZSI6Ik1PUkdBTiIsImZhbWlseV9uYW1lIjoiTVVUVUdJIn0.a2o22M5L9cqemUk3Zi72bIiJKVwfHH5NAMT51AZ2s4BWp0hKplxK5HCjKpkPBi4GeJd0ZyUZUuI31lqlnS81PpMR5ZmrrPxfeuNnlVeSUTtpxzBEOW6zaVa9S7sbnDyS_7RDTubUCDNGZesDxUYtRu1fqNWr5390HJ8Wf20pLxzzCdhKhAe49lG12RA4TCyqGR0svhozVwwQuBBGA59DkYbti7XnD6NOQr8KOIr-Q_HDVk3bfD_8EchM9iQXY3RWZwb1ZrEmjjeCuixI4OwQt8DiPiDYa-LTsA2g5P6IQv_4jt46CLLviFV25Cff14ygRO4TAQNtFtyHImdOzlYQtw",
            refId = "Fst1SUS1PtgVetO6"
        ))
    }
}