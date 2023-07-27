package com.presta.customer

 expect  class ContactsUtils(context: CustomContext) {
     fun getContactList(): List<Contact>
     companion object {
         val READ_CONTACTS_PERMISSION_REQUEST_CODE: Int
     }
 }