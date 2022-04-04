package com.example.tasks.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.R
import com.example.tasks.databinding.ActivityContactsBinding

class ContactsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactsBinding

    companion object {
        const val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val resolver: ContentResolver = contentResolver
        val cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")
        loadContacts(cursor)
    }

    private fun loadContacts(cursors: Cursor?) {
        val builder: StringBuilder

        // Asks permission to access contacts
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS)
        } else {
            builder = getContacts(cursors)
            binding.listContacts.text = builder.toString()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Checks weather permission granted or not
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val resolver: ContentResolver = contentResolver
                val cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")
                loadContacts(cursor)
            } else {
                Toast.makeText(applicationContext,"Permission must be granted in order to display contacts information",Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("Range")
    private fun getContacts(cursors: Cursor?): StringBuilder {
        val builder = StringBuilder()

        // Reads all contacts one by one
        if (cursors != null) {
            if (cursors.count > 0) {
                while (cursors.moveToNext()) {
                    val id =
                        cursors.getString(cursors.getColumnIndex(ContactsContract.Contacts._ID))
                    val name =
                        cursors.getString(cursors.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phoneNumber = (cursors.getString(
                        cursors.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                    )).toInt()

                    if (phoneNumber > 0) {
                        val cursorPhone = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            arrayOf(id),
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
                        )

                        if (cursorPhone != null) {
                            if (cursorPhone.count > 0) {
                                while (cursorPhone.moveToNext()) {
                                    val phoneNumValue = cursorPhone.getString(
                                        cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                    )
                                    builder.append("Contact: ").append(name)
                                        .append(", Phone Number: ").append(
                                            phoneNumValue
                                        ).append("\n\n")
                                }
                            }
                        }
                        cursorPhone?.close()
                    }
                }
            } else {
                Toast.makeText(applicationContext, "No contacts available!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        cursors?.close()
        return builder
    }

    // menu bar for sort contacts
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.contact_sort_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_contacts -> {
                val resolver: ContentResolver = contentResolver
                val cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")
                loadContacts(cursor)
                true
            }
            R.id.unsort_contacts -> {
                val resolver: ContentResolver = contentResolver
                val cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
                    null)
                loadContacts(cursor)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}