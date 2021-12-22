package com.example.tasks.realm

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.R

//data will be stored and shown when we reopen application

class SharedData : AppCompatActivity() {

    private val sharedPrefFile = "kotlinsharedpreference"

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
        }

        view.setOnClickListener{
            val sharedIdValue = sharedPreferences.getInt("id_key",0)
            val sharedNameValue = sharedPreferences.getString("name_key","defaultname")
            if(sharedIdValue == 0 &&sharedNameValue.equals("defaultname")) {
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