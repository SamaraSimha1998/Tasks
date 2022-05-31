package com.example.tasks.imageCompressor

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
import com.example.tasks.databinding.ActivityImageCompressorBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

@Suppress("DEPRECATION")
class ImageCompressorActivity : AppCompatActivity() {

    private val pickImage = 100
    private lateinit var filepath: String
    private lateinit var originalImage: File
    private lateinit var compressedImage: File
    // This will save the processed images into given folder
    private var path: File = File(Environment.getExternalStorageDirectory().absolutePath + "/Pictures")
    private lateinit var binding: ActivityImageCompressorBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageCompressorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        filepath = path.absolutePath

        if(path.exists()){
            path.mkdirs()
        }

        // Reads seek bar value
        binding.seekQualityValue.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                binding.seekQualityValue.max = 100
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
            }
        })

        binding.btnSelectImage.setOnClickListener {
            openGallery()
        }

        binding.btnCompressImage.setOnClickListener {
            val quality = binding.seekQualityValue.progress
            // can change image width * height from here
            val width = 480
            val height = 800

            try {

                // Triggers image compressor from here
                compressedImage = com.example.tasks.nativeImageCompressor.Compressor(this)
                    .setMaxWidth(width)
                    .setMaxHeight(height)
                    .setQuality(quality)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(filepath)
                    .compressToFile(originalImage)

                val finalFile = File(filepath, originalImage.name)
                // calls bitmap factory to decode image file
                val finalBitmap: Bitmap = BitmapFactory.decodeFile(finalFile.absolutePath)
                binding.compressedImage.setImageBitmap(finalBitmap)
                binding.compressedSizeText.text =
                    "size: "+ Formatter.formatShortFileSize(this, finalFile.length())

            } catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    private fun openGallery(){
        // Opens gallery for selecting image
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)
    }

    // Processes selected image
    @Deprecated("Deprecated in Java")
    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            binding.seekQualityValue.visibility = View.VISIBLE
            binding.btnCompressImage.visibility = View.VISIBLE
            val imageUri: Uri? = data?.data
            try {
                val imageStream: InputStream? = imageUri?.let { contentResolver.openInputStream(it)}
                val selectedImage: Bitmap = BitmapFactory.decodeStream(imageStream)
                binding.selectedImage.setImageBitmap(selectedImage)
                originalImage = File(imageUri!!.path!!.replace("raw/",""))
                binding.originalSizeText.text = "size: "+ Formatter.formatFileSize(this, originalImage.length())

            } catch (e: FileNotFoundException){
                e.printStackTrace()
                Toast.makeText(this,"Something Went Wrong!", Toast.LENGTH_SHORT).show()
            }
        }else {
            Toast.makeText(this,"No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }
}