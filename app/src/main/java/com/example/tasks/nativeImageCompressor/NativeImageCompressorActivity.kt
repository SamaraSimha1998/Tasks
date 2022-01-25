package com.example.tasks.nativeImageCompressor

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.format.Formatter
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.R
import kotlinx.android.synthetic.main.activity_native_image_compressor.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

class NativeImageCompressorActivity : AppCompatActivity() {

    private val pickImage = 100
    private lateinit var filepath: String
    private lateinit var originalImage: File
    private lateinit var compressedImage: File
    private var path: File = File(Environment.getExternalStorageDirectory().absolutePath + "/Pictures")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_image_compressor)

        filepath = path.absolutePath

        if(path.exists()){
            path.mkdirs()
        }

        seekQuality.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                seekQuality.max = 100
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
            }
        })

        btn_select.setOnClickListener {
            openGallery()
        }

        btn_compress.setOnClickListener {
            val quality = seekQuality.progress
            val width = 480
            val height = 800

            try {
                compressedImage = Compressor(this)
                    .setMaxWidth(width)
                    .setMaxHeight(height)
                    .setQuality(quality)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(filepath)
                    .compressToFile(originalImage)

                val finalFile = File(filepath, originalImage.name)
                val finalBitmap: Bitmap = BitmapFactory.decodeFile(finalFile.absolutePath)
                compressed_image_view.setImageBitmap(finalBitmap)
                compressed_size_text_view.text =
                    "size: "+ Formatter.formatShortFileSize(this, finalFile.length())

            } catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    private fun openGallery(){
        val gallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            seekQuality.visibility = View.VISIBLE
            btn_compress.visibility = View.VISIBLE
            val imageUri: Uri? = data?.data
            try {
                val imageStream: InputStream? = imageUri?.let { contentResolver.openInputStream(it)}
                val selectedImage: Bitmap = BitmapFactory.decodeStream(imageStream)
                selected_image_view.setImageBitmap(selectedImage)
                originalImage = File(imageUri!!.path!!.replace("raw/",""))
                original_size_text_view.text = "size: "+ Formatter.formatFileSize(this, originalImage.length())

            } catch (e: FileNotFoundException){
                e.printStackTrace()
                Toast.makeText(this,"Something Went Wrong!",Toast.LENGTH_SHORT).show()
            }
        }else {
            Toast.makeText(this,"No Image Selected",Toast.LENGTH_SHORT).show()
        }
    }
}