package com.presta.customer.organisation

import com.presta.customer.MR
import kotlin.native.concurrent.ThreadLocal

class OrganisationModel {
    @ThreadLocal
    companion object Data {
        var organisation: Organisation = Organisation (
            "mobiflex",
            "t10007",
            MR.images.prestalogo,
            MR.images.prestalogodark
        )

        fun  loadOrganisation(org: Organisation) {
            organisation = org
        }
    }

}