package com.presta.customer.ui.components.addGuarantors

import com.presta.customer.Platform

interface AddGuarantorsComponent {
    val platform: Platform
    //val androidContactsHandler: AndroidContactsHandler
    fun onBackNavClicked()
    fun onContinueSelected()

}