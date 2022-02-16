package com.example.tasks.realm

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.R
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_realm_demo.*
import kotlinx.android.synthetic.main.realm_data_input.*

class RealmDemo : AppCompatActivity(), View.OnClickListener {

    private var realm: Realm? = null
    private val dataModel = RealmDataModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_realm_demo)

        Realm.init(this)

        realm = Realm.getDefaultInstance()

        btn_insert_data.setOnClickListener(this)
        btn_read_data.setOnClickListener(this)
        btn_update_data.setOnClickListener(this)
        btn_delete_data.setOnClickListener(this)

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

            dataModel.id = edt_id.text.toString().toInt()
            dataModel.name = edt_name.text.toString()
            dataModel.email = edt_email.text.toString()

            edt_id.setText(""+dataModel.id)
            edt_name.setText(dataModel.name)
            edt_email.setText(dataModel.email)

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
                edt_id?.setText("" + dataModels[i].id)
                edt_name?.setText(dataModels[i].name)
                edt_email?.setText(dataModels[i].email)
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
            val id: Long = edt_id.text.toString().toLong()
            val dataModel = realm!!.where(RealmDataModel::class.java).equalTo("id", id).findFirst()

            edt_name.setText(dataModel?.name)
            edt_email.setText(dataModel?.email)
            Toast.makeText(this, "Data Updated !!!", Toast.LENGTH_SHORT).show()
//            Log.d("Status","Data Updated !!!")
        }catch (e:Exception){
            Toast.makeText(this, "Something went wrong !!!", Toast.LENGTH_SHORT).show()
//            Log.d("Status","Something went Wrong !!!")
        }
    }

    private fun deleteData() {

        try {
            val id: Long = edt_id.text.toString().toLong()
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

        edt_id.setText("")
        edt_name.setText("")
        edt_email.setText("")
    }
}