package com.presta.customer.organisation

import dev.icerock.moko.resources.ImageResource

data class Organisation(
    val tenant_name: String,
    val tenant_id: String,
    val logo: ImageResource,
    val sandbox: Boolean = true
)


