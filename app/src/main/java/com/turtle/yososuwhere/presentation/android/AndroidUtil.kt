package com.turtle.yososuwhere.presentation.android

import android.app.ActivityManager
import android.content.Context
import com.turtle.yososuwhere.presentation.android.di.qualifier.ApplicationContext
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AndroidUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    fun saveLog(log: String) {
        try {
            val now: String =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val buf = BufferedWriter(FileWriter(File(context.filesDir, "log.txt"), true))
            buf.append("$now ") // 날짜 쓰기
            buf.append(log) // 파일 쓰기
            buf.newLine() // 개행
            buf.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}