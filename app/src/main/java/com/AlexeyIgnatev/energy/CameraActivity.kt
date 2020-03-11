package com.AlexeyIgnatev.energy

import android.content.Intent
import android.hardware.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.home_icon.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class CameraActivity : AppCompatActivity() {
    private val TAG = "CameraActivity"
    private lateinit var cameraPreview: CameraPreview
    private lateinit var photoPath: String
    private var checkedItem: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        val camera = Camera.open(findFrontFacingCamera())
        camera.setDisplayOrientation(90)

        checkedItem = intent.getIntExtra("checkedItem", 0)

        cameraPreview = CameraPreview(this, camera)

        frame_layout.addView(cameraPreview)

        home_icon.setOnClickListener {
            startActivity(BackActivity::class.java)
        }

        camera_btn.setOnClickListener {
            camera.takePicture(null, null, mPicture)
        }

    }

    private fun findFrontFacingCamera(): Int {
        var cameraId = -1
        val number = Camera.getNumberOfCameras()
        for (i in 0 until number) {
            val info = Camera.CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                cameraId = i
                break
            }
        }
        return cameraId
    }

    private val mPicture = Camera.PictureCallback { data, _ ->
        val pictureFile: File = createImageFile()
        try {
            val fos = FileOutputStream(pictureFile)
            photoPath = pictureFile.absolutePath
            fos.write(data)
            fos.close()
            startLoadingActivity(photoPath)
        } catch (e: FileNotFoundException) {
            Log.d(TAG, "File not found: ${e.message}")
        } catch (e: IOException) {
            Log.d(TAG, "Error accessing file: ${e.message}")
        }
    }

    private fun startLoadingActivity(path: String) {
        val intent = Intent(this, LoadingActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        intent.putExtra("path", path)
        intent.putExtra("checkedItem", checkedItem)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

}
