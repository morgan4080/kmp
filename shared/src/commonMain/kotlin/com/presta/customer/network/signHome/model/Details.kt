package com.presta.customer.network.signHome.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable


@Serializable
data class Details @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val value: String?=null,
    @EncodeDefault val type: String?=null
)