package network

import organisation.OrganisationModel

object NetworkConstants {
    const val baseUrl = "https://eguarantorship-api.presta.co.ke/api/v1/"
    const val baseUrlKeycloak = "https://iam.presta.co.ke/auth/realms/"

    object PrestaAuthenticateClient {
        val route = baseUrlKeycloak + OrganisationModel.organisation.tenant_id + "protocol/openid-connect/token"
    }

    object PrestaOnBoardingClient {
        const val route = baseUrl + "core-banking/member-details"
    }
}