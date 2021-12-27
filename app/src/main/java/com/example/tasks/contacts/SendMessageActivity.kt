package com.example.tasks.contacts

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.onegravity.contactpicker.contact.ContactDescription
import com.onegravity.contactpicker.contact.ContactSortOrder
import com.onegravity.contactpicker.core.ContactPickerActivity
import com.onegravity.contactpicker.picture.ContactPictureType
import kotlinx.android.synthetic.main.activity_contacts.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_send_message.*


const val REQUEST_CONTACT = 2000

class SendMessageActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.tasks.R.layout.activity_send_message)

        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_CONTACTS,
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) { /* ... */
                }
            }).check()

        btn_contacts_viewer.setOnClickListener {
            val intent = Intent(this@SendMessageActivity, ContactsActivity::class.java)
            startActivity(intent)
        }

        btn_send_message.setOnClickListener {
            myMessage()
        }

        btn_add_contacts.setOnClickListener {
            addContacts()
        }
    }

    private fun addContacts() {
        try {
            val intent = Intent(this, ContactPickerActivity::class.java)
                .putExtra(
                    ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE,
                    ContactPictureType.ROUND.name
                )
                .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, true)
                .putExtra(
                    ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION,
                    ContactDescription.ADDRESS.name
                )
                .putExtra(
                    ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE,
                    ContactsContract.CommonDataKinds.Email.TYPE_WORK
                )
                .putExtra(
                    ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER,
                    ContactSortOrder.AUTOMATIC.name
                )
            startActivityForResult(intent, REQUEST_CONTACT)
            Toast.makeText(this, "Select contacts", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun myMessage() {
        val myNumber: String = contacts_edit_text.text.toString().trim()
        val myMsg: String = message_edit_text.text.toString().trim()
        if (myNumber == "" || myMsg == "") {
            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show()
        } else {
            if (TextUtils.isDigitsOnly(myNumber)) {
                val smsManager: SmsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(myNumber, null, myMsg, null, null)
                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter the correct number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CONTACT && resultCode == RESULT_OK && data != null && data.hasExtra(
                ContactPickerActivity.RESULT_CONTACT_DATA
            )
        ) {

            // we got a result from the contact picker

            // process contacts
            val contacts: List<*>? =
                data.getSerializableExtra(ContactPickerActivity.RESULT_CONTACT_DATA) as List<*>?
            repeat(contacts!!.size) {
                // process the contacts...
            }

            // process groups
            val groups: List<*>? =
                data.getSerializableExtra(ContactPickerActivity.RESULT_GROUP_DATA) as List<*>?
            if (groups != null) {
                repeat(groups.size) {
                    // process the groups...
                }
            }
        }
    }

}
