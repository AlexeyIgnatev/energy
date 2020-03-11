package com.AlexeyIgnatev.energy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.home_icon.*

class ShareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        home_icon.setOnClickListener {
            startActivity(BackActivity::class.java)
        }
    }
}
