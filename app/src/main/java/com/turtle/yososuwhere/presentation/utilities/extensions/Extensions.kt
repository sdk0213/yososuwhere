package com.turtle.yososuwhere.presentation.utilities.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Insets
import android.graphics.Point
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.IBinder
import android.text.Editable
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import java.text.SimpleDateFormat
import java.util.*

fun Date.convertDateToStringTimeStamp(): String =
    SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(this)

fun Date.convertDateToStringHMSTimeStamp(): String =
    SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(this)

fun Date.convertDateToStringyyyyMMddTimeStamp(): String =
    SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(this)

fun Date.convertDateToStringyyMMddTimeStampWithSlash(): String =
    SimpleDateFormat("yy/MM/dd", Locale.getDefault()).format(this)

fun Date.convertDateToStringMMddHHmmTimeStamp(): String =
    SimpleDateFormat("MM/dd HH:mm", Locale.getDefault()).format(this)

fun Date.convertDateToStringHHmmTimeStamp(): String =
    SimpleDateFormat("HH:mm", Locale.getDefault()).format(this)


fun Date.getCalendarWithoutTime(): Calendar {
    val date = this
    return GregorianCalendar().apply {
        time = date
        set(Calendar.HOUR, 0);
        set(Calendar.HOUR_OF_DAY, 0);
        set(Calendar.MINUTE, 0);
        set(Calendar.SECOND, 0);
        set(Calendar.MILLISECOND, 0);
    }
}

fun String.toEditable(): Editable =
    Editable.Factory.getInstance().newEditable(this)

fun CharSequence.toEditable(): Editable =
    Editable.Factory.getInstance().newEditable(this)

fun String.convertToDateyyyyMMddHHmm(): Date =
    SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).parse(this)!!

fun String.convertToDateyyyyMMdd(): Date =
    SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).parse(this)!!

fun String.convertToDateHHmm(): Date =
    SimpleDateFormat("HHmm", Locale.getDefault()).parse(this)!!

fun String.convertYYYYMMDD(): String =
    SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(
        SimpleDateFormat("yyyyMMdd", Locale.getDefault()).parse(this)!!
    )

fun String.convertHHmm(): String =
    SimpleDateFormat("HH시 mm분", Locale.getDefault()).format(
        SimpleDateFormat("HHmm", Locale.getDefault()).parse(this)!!
    )

fun Date.getCountDay(endDate: Date): Long =
    ((this.time - endDate.time) / (24 * 60 * 60 * 1000)) + 1

fun Context.hideKeyboard(windowToken: IBinder) =
    (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        windowToken,
        0
    )

fun Context.showKeyboard(view: View) =
    (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
        view,
        0
    )

// 윈도우 사이즈 계산
fun WindowManager.currentWindowMetricsPointCompat(): Point {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowInsets = currentWindowMetrics.windowInsets
        var insets: Insets = windowInsets.getInsets(WindowInsets.Type.navigationBars())
        windowInsets.displayCutout?.run {
            insets = Insets.max(
                insets,
                Insets.of(safeInsetLeft, safeInsetTop, safeInsetRight, safeInsetBottom)
            )
        }
        val insetsWidth = insets.right + insets.left
        val insetsHeight = insets.top + insets.bottom
        Point(
            currentWindowMetrics.bounds.width() - insetsWidth,
            currentWindowMetrics.bounds.height() - insetsHeight
        )
    } else {
        Point().apply {
            defaultDisplay.getSize(this)
        }
    }
}

// DialogFragment --> Fragment 데이터 전달
fun <T> Fragment.setNavigationResult(key: String, value: T) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(
        key,
        value
    )
}

// DialogFragment --> Fragment 데이터 전달
fun <T> Fragment.getNavigationResult(@IdRes id: Int, key: String, onResult: (result: T) -> Unit) {
    val navBackStackEntry = findNavController().getBackStackEntry(id)

    val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_RESUME
            && navBackStackEntry.savedStateHandle.contains(key)
        ) {
            val result = navBackStackEntry.savedStateHandle.get<T>(key)
            result?.let(onResult)
            navBackStackEntry.savedStateHandle.remove<T>(key)
        }
    }
    navBackStackEntry.lifecycle.addObserver(observer)

    viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            navBackStackEntry.lifecycle.removeObserver(observer)
        }
    })
}

@SuppressLint("MissingPermission")
fun Context.isConnected(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val nw = connectivityManager.activeNetwork ?: return false
    val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
    return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}