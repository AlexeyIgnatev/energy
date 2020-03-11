package com.AlexeyIgnatev.energy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var checkedItem: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkedItem = intent.getIntExtra("checkedItem", 0)

        start_btn.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putExtra("checkedItem", checkedItem)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        change_theme_btn.setBackgroundResource(0)

        change_theme_btn.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Выберите тему")
        val strings = resources.getStringArray(R.array.themes)
        builder.setSingleChoiceItems(
            strings,
            checkedItem
        ) { _, which ->
            when (which) {
                0 -> checkedItem = 0
                1 -> checkedItem = 1
                2 -> checkedItem = 2
            }
        }

        builder.setPositiveButton(android.R.string.ok) {_, _ ->

        }

        val alert = builder.create()
        alert.show()
    }

}
