package com.presta.customer.organisation

import com.presta.customer.MR
import kotlin.native.concurrent.ThreadLocal

class OrganisationModel {
    @ThreadLocal
    companion object Data {
        var organisation: Organisation = Organisation (
            "pcea ruiru sacco",
            "t10091",
            MR.images.prestalogo,
            MR.images.prestalogodark
        )`

        fun  loadOrganisation(org: Organisation) {
            organisation = org
        }
    }

}