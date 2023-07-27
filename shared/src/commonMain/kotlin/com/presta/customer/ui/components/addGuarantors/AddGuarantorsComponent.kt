package com.presta.customer.ui.components.addGuarantors

import com.presta.customer.ContactsUtils
import com.presta.customer.Platform

interface AddGuarantorsComponent {
    val platform: Platform
    val contactlist: ContactsUtils

    fun onBackNavClicked()
    fun onContinueSelected()

}