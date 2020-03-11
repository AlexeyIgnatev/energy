package com.AlexeyIgnatev.energy

import java.util.*

class MyTimerTask(private val listener: Listener) : TimerTask() {
    interface Listener {
        fun onTime()
    }

    override fun run() {
        listener.onTime()
    }
}