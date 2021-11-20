package com.turtle.yososuwhere

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isTaskRoot &&
            intent?.run {
                hasCategory(Intent.CATEGORY_LAUNCHER)
                action?.equals(Intent.ACTION_MAIN)
            } == true
        ) {
            finish()
            return
        }

    }

}