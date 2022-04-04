package com.example.tasks.realm

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.R
import com.example.tasks.databinding.ActivityRealmDemoBinding
import io.realm.Realm

class RealmDemo : AppCompatActivity(), View.OnClickListener {

    private var realm: Realm? = null
    private val dataModel = RealmDataModel()
    private lateinit var binding: ActivityRealmDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRealmDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Realm.init(this)

        realm = Realm.getDefaultInstance()

        binding.btnInsertData.setOnClickListener(this)
        binding.btnReadData.setOnClickListener(this)
        binding.btnUpdateData.setOnClickListener(this)
        binding.btnDeleteData.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.btn_insert_data -> {
                addData()
            }

            R.id.btn_read_data -> {
                readData()
            }

            R.id.btn_update_data -> {
                updateData()
            }

            R.id.deltaRelative -> {
                deleteData()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun addData() {

        try {

            dataModel.id = findViewById<EditText>(R.id.edt_id).text.toString().toInt()
            dataModel.name = findViewById<EditText>(R.id.edt_name).text.toString()
            dataModel.email = findViewById<EditText>(R.id.edt_email).text.toString()

            findViewById<EditText>(R.id.edt_id).setText(""+dataModel.id)
            findViewById<EditText>(R.id.edt_name).setText(dataModel.name)
            findViewById<EditText>(R.id.edt_email).setText(dataModel.email)

            // Copies inserted data to data model
            realm!!.executeTransaction { realm -> realm.copyToRealm(dataModel) }

            clearFields()
            Toast.makeText(this, "Data Inserted !!!", Toast.LENGTH_SHORT).show()
//            Log.d("Status","Data Inserted !!!")

        }catch (e:Exception){
            Toast.makeText(this, "Something went wrong !!!", Toast.LENGTH_SHORT).show()
//            Log.d("Status","Something went Wrong !!!")
        }
    }

    @SuppressLint("SetTextI18n")
    fun readData() {

        try {

            // Reads all data from data model
            val dataModels: List<RealmDataModel> =
                realm!!.where(RealmDataModel::class.java).findAll()

            // Sets data of particular id to view
            for (i in dataModels.indices) {
                findViewById<EditText>(R.id.edt_id)?.setText("" + dataModels[i].id)
                findViewById<EditText>(R.id.edt_name)?.setText(dataModels[i].name)
                findViewById<EditText>(R.id.edt_email)?.setText(dataModels[i].email)
            }
            Toast.makeText(this, "Data Fetched !!!", Toast.LENGTH_SHORT).show()
//            Log.d("Status","Data Fetched !!!")

        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong !!!", Toast.LENGTH_SHORT).show()
//            Log.d("Status","Something went Wrong !!!")
        }
    }

    private fun updateData() {

        try {
            val id: Long = findViewById<EditText>(R.id.edt_id).text.toString().toLong()
            val dataModel = realm!!.where(RealmDataModel::class.java).equalTo("id", id).findFirst()

            findViewById<EditText>(R.id.edt_name).setText(dataModel?.name)
            findViewById<EditText>(R.id.edt_email).setText(dataModel?.email)
            Toast.makeText(this, "Data Updated !!!", Toast.LENGTH_SHORT).show()
//            Log.d("Status","Data Updated !!!")
        }catch (e:Exception){
            Toast.makeText(this, "Something went wrong !!!", Toast.LENGTH_SHORT).show()
//            Log.d("Status","Something went Wrong !!!")
        }
    }

    private fun deleteData() {

        try {
            val id: Long = findViewById<EditText>(R.id.edt_id).text.toString().toLong()
            val dataModel = realm!!.where(RealmDataModel::class.java).equalTo("id", id).findFirst()
            realm!!.executeTransaction {
                dataModel?.deleteFromRealm()
            }
            clearFields()
            Toast.makeText(this, "Data deleted !!!", Toast.LENGTH_SHORT).show()
//            Log.d("Status","Data deleted !!!")

        }catch (e:Exception){
            Toast.makeText(this, "Something went wrong !!!", Toast.LENGTH_SHORT).show()
//            Log.d("Status","Something went Wrong !!!")
        }
    }

    private fun clearFields(){

        findViewById<EditText>(R.id.edt_id).setText("")
        findViewById<EditText>(R.id.edt_name).setText("")
        findViewById<EditText>(R.id.edt_email).setText("")
    }
}