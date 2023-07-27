package com.presta.customer

 expect  class ContactsUtils(context: AndroidContext) {
     fun getContactList(): List<Contact>
     companion object {
         val READ_CONTACTS_PERMISSION_REQUEST_CODE: Int
     }
 }