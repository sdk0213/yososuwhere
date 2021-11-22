package com.turtle.yososuwhere.presentation.view.main

import android.content.Intent
import android.os.Bundle
import com.turtle.yososuwhere.databinding.ActivityMainBinding
import dagger.Lazy
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var binding: Lazy<ActivityMainBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.get().lifecycleOwner = this

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