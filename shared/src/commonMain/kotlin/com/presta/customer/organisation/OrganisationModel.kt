package com.presta.customer.organisation

import com.presta.customer.MR
import kotlin.native.concurrent.ThreadLocal

class OrganisationModel {
    @ThreadLocal
    companion object Data {
        var organisation: Organisation = Organisation (
            "Ketepa Sacco",
            "t55049",
            MR.images.logo,
            MR.images.logodark,
            false,
        )

        fun  loadOrganisation(org: Organisation) {
            organisation = org
        }
    }

}