package com.presta.customer.network.authDevice.client

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.HttpHeaders
import io.ktor.http.formUrlEncode
import com.presta.customer.network.NetworkConstants
import com.presta.customer.network.authDevice.errorHandler.authErrorHandler
import com.presta.customer.network.authDevice.model.PrestaAuthResponse
import com.presta.customer.network.authDevice.model.PrestaCheckAuthUserResponse
import com.presta.customer.network.authDevice.model.PrestaCheckPinResponse
import com.presta.customer.network.authDevice.model.PrestaLogInResponse

class PrestaAuthClient(
    private val httpClient: HttpClient
) {
    suspend fun authClient(
        client_secret: String,
    ): PrestaAuthResponse {
        return authErrorHandler {
            httpClient.post(NetworkConstants.PrestaAuthenticateClient.route) {
                header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(listOf(
                    "client_id" to "direct-access",
                    "grant_type" to "client_credentials",
                    "client_secret" to client_secret
                ).formUrlEncode())
            }
        }
    }
    suspend fun checkUserPin(
        token: String,
        phoneNumber: String
    ): PrestaCheckPinResponse {
        return authErrorHandler {
            httpClient.get("${NetworkConstants.PrestaCheckPinClient.route}/${phoneNumber}") {
                header(HttpHeaders.Authorization, "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }
        }
    }
    suspend fun loginUser(
        phoneNumber: String,
        pin: String,
        clientSecret: String
    ): PrestaLogInResponse {
        return authErrorHandler {
            httpClient.post(NetworkConstants.PrestaAuthenticateUser.route) {
                header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(listOf(
                    "phoneNumber" to phoneNumber,
                    "ussdpin" to pin,
                    "client_id" to "direct-access",
                    "client_secret" to clientSecret,
                    "grant_type" to "password",
                    "scope" to "openid",
                ).formUrlEncode())
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
}