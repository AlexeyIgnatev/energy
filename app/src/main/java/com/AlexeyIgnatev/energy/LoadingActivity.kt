package com.AlexeyIgnatev.energy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_loading.*
import java.util.*

class LoadingActivity : AppCompatActivity(), MyTimerTask.Listener {
    private var path: String? = null
    private var checkedItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val timer = Timer()
        timer.schedule(MyTimerTask(this), 3000)

        val anim = AnimationUtils.loadAnimation(this, R.anim.rotare)
        loading_circle.startAnimation(anim)
        path = intent.getStringExtra("path")
        checkedItem = intent.getIntExtra("checkedItem", 0)
    }

    override fun onTime() {
        val intent = Intent(this, EditPhotoActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        intent.putExtra("path", path)
        intent.putExtra("checkedItem", checkedItem)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }
}
