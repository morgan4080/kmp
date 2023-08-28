package com.presta.customer.network.authDevice.client

import com.presta.customer.network.NetworkConstants
import com.presta.customer.network.authDevice.errorHandler.authErrorHandler
import com.presta.customer.network.authDevice.model.PrestaCheckAuthUserResponse
import com.presta.customer.network.authDevice.model.PrestaLogInResponse
import com.presta.customer.network.authDevice.model.RefreshTokenResponse
import com.presta.customer.network.authDevice.model.TenantServiceConfigResponse
import com.presta.customer.network.authDevice.model.TenantServicesResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

class PrestaAuthClient(
    private val httpClient: HttpClient
) {
    suspend fun loginUser(
        phoneNumber: String,
        pin: String,
        tenantId: String
    ): PrestaLogInResponse {
        return authErrorHandler {
            httpClient.post(NetworkConstants.PrestaAuthenticateUser.route) {
                contentType(ContentType.Application.Json)
                setBody(
                    LoginUserData (
                        phoneNumber = phoneNumber,
                        pin = pin
                    )
                )
                url {
                    parameters.append("tenantId", tenantId)
                }
            }
        }
    }
    suspend fun updateAuthToken(
        refreshToken: String,
        tenantId: String?
    ): RefreshTokenResponse {
        return authErrorHandler {
            httpClient.post(NetworkConstants.PrestaRefreshToken.route) {
                contentType(ContentType.Application.Json)
                setBody(
                    RefreshTokenData (
                        refresh_token = refreshToken
                    )
                )
                url {
                    if (tenantId != null) {
                        parameters.append("tenantId", tenantId)
                    }
                }
            }
        }
    }

    suspend fun checkAuthUser(token: String): PrestaCheckAuthUserResponse {
        return authErrorHandler {
            httpClient.get(NetworkConstants.PrestaCheckAuthUser.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }
        }
    }

    suspend fun checkTenantServices(token: String, tenantId: String): List<TenantServicesResponse> {
        return authErrorHandler {
            httpClient.get(NetworkConstants.PrestaServices.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("tenantId", tenantId)
                }
            }
        }
    }

    suspend fun checkTenantServicesConfig(token: String, tenantId: String): List<TenantServiceConfigResponse> {
        return authErrorHandler {
            httpClient.get(NetworkConstants.PrestaServicesConfig.route) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("tenantId", tenantId)
                }
            }
        }
    }

    @Serializable
    data class LoginUserData(
        val phoneNumber: String,
        val pin: String,
    )

    @Serializable
    data class RefreshTokenData(
        val refresh_token: String,
    )
}