package com.presta.customer.network.authDevice.data.dbMapper

import com.presta.customer.network.authDevice.model.PrestaLogInResponse
import comprestacustomer.UserAuthEntity

fun PrestaLogInResponse.toUserAuthEntity() = UserAuthEntity(
    access_token = access_token,
)