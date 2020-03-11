package com.AlexeyIgnatev.energy

import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_edit_photo.*
import kotlinx.android.synthetic.main.home_icon.*
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class EditPhotoActivity : AppCompatActivity() {
    private var path: String? = null
    private val TAG = "EditPhotoActivity"
    private lateinit var bitmap: Bitmap
    private var checkedItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_photo)

        home_icon.setOnClickListener {
            startActivity(BackActivity::class.java)
        }

        path = intent.getStringExtra("path")
        checkedItem = intent.getIntExtra("checkedItem", 0)

        Log.d(TAG, "onCreate: $path")

        make_again_btn.setOnClickListener {
            startActivity(CameraActivity::class.java)
            finish()
        }

        send_email_btn.setOnClickListener {
            val file = createImageFile()
            try {
                val fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
                path = file.absolutePath
                fos.close()
                startEmailActivity()
            } catch (e: FileNotFoundException) {
                Log.d(TAG, "File not found: ${e.message}")
            } catch (e: IOException) {
                Log.d(TAG, "Error accessing file: ${e.message}")
            }
        }

        createImage()
        image.setImageBitmap(bitmap)
    }

    private fun createImage() {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        bitmap = BitmapFactory.decodeFile(path, options)

        val matrix = Matrix()
        matrix.postRotate(-90f)
        matrix.postScale(-1f, 1f, bitmap.width/2f, bitmap.height/2f)
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

        var redFilter = BitmapFactory.decodeResource(resources, R.drawable.red_filter)
        redFilter = Bitmap.createScaledBitmap(redFilter, bitmap.width, bitmap.height, true)

        var blackFilter = BitmapFactory.decodeResource(resources, R.drawable.black_filter)
        blackFilter = Bitmap.createScaledBitmap(blackFilter, bitmap.width, bitmap.height, true)

        var canvas = Canvas(bitmap)
        canvas.drawBitmap(redFilter, Matrix(), null)

        bitmap = ColorDodge.ColorDodgeBlend(blackFilter, bitmap)

        var source = 0
        when(checkedItem) {
            0 -> {
                source = listOf(R.drawable.bc1, R.drawable.bc2,  R.drawable.bc3, R.drawable.bc4, R.drawable.bc5, R.drawable.bc6).random()
            }
            1 -> {
                source = listOf(R.drawable.sm1, R.drawable.sm2,  R.drawable.sm3, R.drawable.sm4, R.drawable.sm5, R.drawable.sm6).random()
            }
            2 -> {
                source = listOf(R.drawable.ap1, R.drawable.ap2,  R.drawable.ap3, R.drawable.ap4, R.drawable.ap5, R.drawable.ap6).random()
            }
        }
        canvas = Canvas(bitmap)
        val text = BitmapFactory.decodeResource(resources, source)
        val marginLeft = (bitmap.width - text.width) / 2f
        val marginTop = (bitmap.height - text.height) - 66f
        canvas.drawBitmap(text, marginLeft, marginTop, null)
    }


    private fun startEmailActivity() {
        val intent = Intent(this, EmailActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        intent.putExtra("path", path)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

}
