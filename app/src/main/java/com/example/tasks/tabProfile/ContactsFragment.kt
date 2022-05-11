package com.example.tasks.tabProfile

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tasks.contacts.ContactsActivity
import com.example.tasks.databinding.FragmentContactsBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.onegravity.contactpicker.contact.ContactDescription
import com.onegravity.contactpicker.contact.ContactSortOrder
import com.onegravity.contactpicker.core.ContactPickerActivity
import com.onegravity.contactpicker.picture.ContactPictureType

@Suppress("DEPRECATION")
class ContactsFragment : Fragment() {
    
    private val requestContact = 2000
    private val argParam1 = "param1"
    private val argParam2 = "param2"
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(argParam1)
            param2 = it.getString(argParam2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        // Third party library to read contacts and send messages
        Dexter.withContext(activity)
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

        binding.btnFragmentContactsViewer.setOnClickListener {
            val intent = Intent(activity, ContactsActivity::class.java)
            startActivity(intent)
        }

        binding.btnFragmentSendMessage.setOnClickListener {
            myMessage()
        }

        binding.btnFragmentAddContacts.setOnClickListener {
            addContacts()
        }
        return binding.root
    }

    // Read contacts from storage and add contacts to the recycler view
    private fun addContacts() {
        try {
            val intent = Intent(activity, ContactPickerActivity::class.java)
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
            startActivityForResult(intent, requestContact)
            Toast.makeText(activity, "Select contacts", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_SHORT).show()
        }
    }

/**    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CONTACT && resultCode == Activity.RESULT_OK){
            var uri: Uri = data?.data?: return
            var cols: Array<String> = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
            var rs: Cursor? = ContentResolver.query(uri,cols,null,null,null)
            if (rs?.moveToFirst()!!){
                fragment_contacts_edit_text(rs.getString(0))
            }
        }
    }
 **/

    // Sends message to given contacts
    private fun myMessage() {
        val myNumber: String = binding.fragmentContactsEditText.text.toString().trim()
        val myMsg: String = binding.fragmentMessageEditText.text.toString().trim()
        if (myNumber == "" || myMsg == "") {
            Toast.makeText(activity, "Field cannot be empty", Toast.LENGTH_SHORT).show()
        } else {
            if (TextUtils.isDigitsOnly(myNumber)) {
                val smsManager: SmsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(myNumber, null, myMsg, null, null)
                Toast.makeText(activity, "Message Sent", Toast.LENGTH_SHORT).show()
                binding.fragmentContactsEditText.setText("")
                binding.fragmentMessageEditText.setText("")
            } else {
                Toast.makeText(activity, "Please enter the correct number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContactsFragment().apply {
                arguments = Bundle().apply {
                    putString(argParam1, param1)
                    putString(argParam2, param2)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}