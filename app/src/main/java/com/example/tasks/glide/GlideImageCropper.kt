package com.example.tasks.glide

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tasks.databinding.ActivityGlideImageCropperBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

@Suppress("DEPRECATION")
class GlideImageCropper : AppCompatActivity() {

    private val requestCode = 42
    private val tag: String = "AppDebug"
    private lateinit var binding: ActivityGlideImageCropperBinding

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGlideImageCropperBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCapture.setOnClickListener {
            // Permission to open camera to capture image
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (intent.resolveActivity(this.packageManager) != null){
                startActivityForResult(intent, requestCode)
            }else{
                Toast.makeText(this, "Unable to open camera",Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnGlideCrop.setOnClickListener {
            val intent = Intent(this@GlideImageCropper, GlideCrop::class.java)
            startActivity(intent)
        }

        binding.updateTextview.setOnClickListener {
            pickFromGallery()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            resultCode -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let { uri ->
                        launchImageCrop(uri)
                    }
                } else {
                    Log.e(tag, "Image selection error: Couldn't select that image from memory.")
                }
            }

            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    setImage(result.uri)
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Log.e(tag, "Crop error: ${result.error}")
                }
            }
        }
    }

    private fun setImage(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .into(binding.imagePreview)
    }

    private fun launchImageCrop(uri: Uri) {
        CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(1920, 1080)
            .setCropShape(CropImageView.CropShape.RECTANGLE) // default is rectangle
            .start(this)
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent, requestCode)

    }
}