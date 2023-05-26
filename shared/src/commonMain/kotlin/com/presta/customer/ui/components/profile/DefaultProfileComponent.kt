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
            token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ3SVFSTGZNLU1fYUNEX1BLSW56Z2haTFB6MDF5NDA1WEJkS0lJRGxFaWdjIn0.eyJleHAiOjE2ODUwNjk4OTYsImlhdCI6MTY4NTA2OTU5NiwianRpIjoiMmZlOTE3NmEtMjUzOC00Zjc0LTkxYWQtNmRiZTZhMzc3MmUzIiwiaXNzIjoiaHR0cHM6Ly9zdGFnaW5nLWlhbS5wcmVzdGEuY28ua2UvYXV0aC9yZWFsbXMvdDEwMDA3IiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImJmMGRlYzg2LWQ3YmUtNDMyZi1iNzA3LTI3Njc2OThlMTNmNCIsInR5cCI6IkJlYXJlciIsImF6cCI6ImxvYW4tYXBwbGljYXRpb25zLXNlcnZpY2UiLCJzZXNzaW9uX3N0YXRlIjoiYTUyYjcyZjAtYmY5Yy00YTg3LWE1NzQtNzkxMWQzZmJhNDg1IiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJ0ZW5hbnRJZCI6InQxMDAwNyIsIm5hbWUiOiJNT1JHQU4gTVVUVUdJIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiY2RiMWI4NjMtMTUyZi00NTdlLWEwZDktZDM1ZWRjNzU3ZTc3IiwiZ2l2ZW5fbmFtZSI6Ik1PUkdBTiIsImZhbWlseV9uYW1lIjoiTVVUVUdJIn0.CpFwx_bScPg5F-fEp2RrArlVBNyHUFU0gejaihDec2v--60f5aPUTT9EIuf82gw4O_kmeve6Vw7mlPnWCdxjjdoWOsB1CKDFzMXnbSI17MNYSDDlVZc_sIX4vwo9_-kPwCmI0Ya6CGofaFMm4QCclhTsoW-m60aOEnE9y1Xpx-igtLzX9E-ef82fDpC8yuAUcFCGfGBiqFSPtEcfquTCD6pazuiC4XClz4v2E-_DnuFd7OlXHzWxTr6jwb4X0cFEWBiCcRFiUCP5LrYNOCmWNRUO06DvRezTfBk5tbBwDeNKGsxCO1BKtY3WxzOoovJVrLDzKmrLakerl2W8hE0GJw",
            refId = "Fst1SUS1PtgVetO6"
        ))
    }
}