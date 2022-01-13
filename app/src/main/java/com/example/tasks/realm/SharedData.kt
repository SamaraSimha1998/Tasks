package com.example.tasks.realm

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.example.tasks.R
import java.io.ByteArrayOutputStream
import java.io.File

class SharedData : AppCompatActivity() {

    private val sharedPrefFile = "kotlinSharedPreference"

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shared_data)

        val inputId = findViewById<EditText>(R.id.editId)
        val inputName = findViewById<EditText>(R.id.editName)
        val outputId = findViewById<TextView>(R.id.textViewShowId)
        val outputName = findViewById<TextView>(R.id.textViewShowName)

        val save = findViewById<Button>(R.id.btn_save)
        val view = findViewById<Button>(R.id.btn_view)
        val clear = findViewById<Button>(R.id.btn_clear)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)

        save.setOnClickListener{
            val id: Int = Integer.parseInt(inputId.text.toString())
            val name: String = inputName.text.toString()
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putInt("id_key",id)
            editor.putString("name_key",name)
            editor.apply()

            val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
            val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

            val fileToRead = "encryptedData.txt"
            val encryptedFile = EncryptedFile.Builder(
                File(sharedPrefFile, fileToRead),
                applicationContext,
                mainKeyAlias,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            try {
                val inputStream = encryptedFile.openFileInput()
                val byteArrayOutputStream = ByteArrayOutputStream()
                var nextByte: Int = inputStream.read()
                while (nextByte != -1) {
                    byteArrayOutputStream.write(nextByte)
                    nextByte = inputStream.read()
                }
            }catch (e: Exception){
            }
        }

        view.setOnClickListener{
            val sharedIdValue = sharedPreferences.getInt("id_key",0)
            val sharedNameValue = sharedPreferences.getString("name_key","defaultName")
            if(sharedIdValue == 0 &&sharedNameValue.equals("defaultName")) {
                outputName.setText("default name: $sharedNameValue").toString()
                outputId.text = "default id: $sharedIdValue"
            }else {
                outputName.setText(sharedNameValue).toString()
                outputId.text = sharedIdValue.toString()
            }
        }

        clear.setOnClickListener{
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            outputName.setText("").toString()
            outputId.text = ""
        }
    }
}