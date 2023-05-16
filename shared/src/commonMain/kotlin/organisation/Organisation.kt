package organisation

import dev.icerock.moko.resources.ImageResource

data class Organisation(
    val tenant_name: String,
    val tenant_id: String,
    val client_secret: String,
    val logo: ImageResource?
)


