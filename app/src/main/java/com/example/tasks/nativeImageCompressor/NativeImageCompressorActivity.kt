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
import com.example.tasks.databinding.ActivityNativeImageCompressorBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

@Suppress("DEPRECATION")
class NativeImageCompressorActivity : AppCompatActivity() {

    private val pickImage = 100
    private lateinit var filepath: String
    private lateinit var originalImage: File
    private lateinit var compressedImage: File
    private var path: File = File(Environment.getExternalStorageDirectory().absolutePath + "/Pictures")
    private lateinit var binding: ActivityNativeImageCompressorBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNativeImageCompressorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        filepath = path.absolutePath

        if(path.exists()){
            path.mkdirs()
        }

        // Reads value from seekbar
        binding.seekQuality.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                binding.seekQuality.max = 100
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
            }
        })

        binding.btnSelect.setOnClickListener {
            openGallery()
        }

        binding.btnCompress.setOnClickListener {
            val quality = binding.seekQuality.progress
            val width = 480
            val height = 800

            try {
                // Starts compressing selected image
                compressedImage = Compressor(this)
                    .setMaxWidth(width)
                    .setMaxHeight(height)
                    .setQuality(quality)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(filepath)
                    .compressToFile(originalImage)

                val finalFile = File(filepath, originalImage.name)
                val finalBitmap: Bitmap = BitmapFactory.decodeFile(finalFile.absolutePath)
                binding.compressedImageView.setImageBitmap(finalBitmap)
                binding.compressedSizeTextView.text =
                    "size: "+ Formatter.formatShortFileSize(this, finalFile.length())

            } catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    // Opens gallery and picks selected image
    private fun openGallery(){
        val gallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            binding.seekQuality.visibility = View.VISIBLE
            binding.btnCompress.visibility = View.VISIBLE
            val imageUri: Uri? = data?.data
            try {
                val imageStream: InputStream? = imageUri?.let { contentResolver.openInputStream(it)}
                val selectedImage: Bitmap = BitmapFactory.decodeStream(imageStream)
                binding.selectedImageView.setImageBitmap(selectedImage)
                originalImage = File(imageUri!!.path!!.replace("raw/",""))
                binding.originalSizeTextView.text = "size: "+ Formatter.formatFileSize(this, originalImage.length())

            } catch (e: FileNotFoundException){
                e.printStackTrace()
                Toast.makeText(this,"Something Went Wrong!",Toast.LENGTH_SHORT).show()
            }
        }else {
            Toast.makeText(this,"No Image Selected",Toast.LENGTH_SHORT).show()
        }
    }
}