package com.presta.customer.network.authDevice.data.dbMapper

import com.presta.customer.network.authDevice.model.PrestaLogInResponse
import comprestacustomer.UserAuthEntity

fun PrestaLogInResponse.toUserAuthEntity() = UserAuthEntity(
    access_token = access_token,
    expires_in = expires_in,
    refresh_expires_in = refresh_expires_in,
    refresh_token = refresh_token,
    token_type = token_type,
    scope = scope
)