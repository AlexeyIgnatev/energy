package com.AlexeyIgnatev.energy

import android.app.Activity
import android.content.Context
import android.content.Intent
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun Activity.startActivity(activity: Class<*>) {
    val intent = Intent(this, activity)
    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
    startActivity(intent)
    overridePendingTransition(0, 0)
}

fun Context.createImageFile(): File {
    val simpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
    val storageDir = applicationContext.cacheDir
    return File.createTempFile(
        "JPEG_${simpleDateFormat.format(Date())}_",
        ".jpg",
        storageDir
    )
}
