package com.example.tasks.glide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R

class GlideCrop : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide_crop)

        findViewById<RecyclerView>(R.id.list).apply {
            layoutManager = LinearLayoutManager(context)

            // Connects to adapter to load image into different guide cropping shapes
            adapter = GlideRecyclerAdapter(context, mutableListOf(
                GlideRecyclerAdapter.Type.Mask,
                GlideRecyclerAdapter.Type.NinePatchMask,
                GlideRecyclerAdapter.Type.RoundedCorners,
                GlideRecyclerAdapter.Type.CropTop,
                GlideRecyclerAdapter.Type.CropCenter,
                GlideRecyclerAdapter.Type.CropBottom,
                GlideRecyclerAdapter.Type.CropSquare,
                GlideRecyclerAdapter.Type.CropCircle,
                GlideRecyclerAdapter.Type.CropCircleWithBorder,
                GlideRecyclerAdapter.Type.Grayscale,
                GlideRecyclerAdapter.Type.BlurLight,
                GlideRecyclerAdapter.Type.BlurDeep,
                GlideRecyclerAdapter.Type.Toon,
                GlideRecyclerAdapter.Type.Sepia,
                GlideRecyclerAdapter.Type.Contrast,
                GlideRecyclerAdapter.Type.Invert,
                GlideRecyclerAdapter.Type.Pixel,
                GlideRecyclerAdapter.Type.Sketch,
                GlideRecyclerAdapter.Type.Swirl,
                GlideRecyclerAdapter.Type.Brightness,
                GlideRecyclerAdapter.Type.Kuawahara,
                GlideRecyclerAdapter.Type.Vignette
            ))
        }
        // here we can add any type of cropping option
    }
}