package com.presta.customer.organisation

import com.presta.customer.MR
import kotlin.native.concurrent.ThreadLocal

class OrganisationModel {
    @ThreadLocal
    companion object Data {
        var organisation: Organisation = Organisation (
            "Presta Customer",
            "t10007",
            MR.images.prestalogo
        )

        fun  loadOrganisation(org: Organisation) {
            organisation = org
        }
    }

}