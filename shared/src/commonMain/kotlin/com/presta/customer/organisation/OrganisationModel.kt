package com.presta.customer.organisation

import com.presta.customer.MR
import kotlin.native.concurrent.ThreadLocal

class OrganisationModel {
    @ThreadLocal
    companion object Data {
        var organisation: Organisation = Organisation (
            "CODO",
            "t11813",
            MR.images.logo,
            MR.images.logodark,
            true,
        )

        fun  loadOrganisation(org: Organisation) {
            organisation = org
        }
    }

}