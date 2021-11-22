package com.turtle.yososuwhere.presentation.utilities

import android.content.Context
import com.turtle.yososuwhere.presentation.android.AndroidUtil
import com.turtle.yososuwhere.presentation.android.di.qualifier.ApplicationContext
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import javax.inject.Inject

class CatchUnexpectedException @Inject constructor(
    @ApplicationContext private val context: Context,
    private val androidUtil: AndroidUtil
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        val out = ByteArrayOutputStream()
        throwable.printStackTrace(PrintStream(out))
        androidUtil.saveLog("Unexpected Exception 발생: \n$out")
    }

}