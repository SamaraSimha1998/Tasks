package com.example.tasks.glide

import android.content.res.Resources

// gets pixel value from image
val Float.px: Float get() = (this * Resources.getSystem().displayMetrics.density)

val Int.px: Int get() = ((this * Resources.getSystem().displayMetrics.density).toInt())