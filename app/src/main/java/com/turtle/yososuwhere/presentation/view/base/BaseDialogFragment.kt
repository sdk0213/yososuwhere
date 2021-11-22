package com.turtle.yososuwhere.presentation.view.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.turtle.yososuwhere.presentation.utilities.extensions.currentWindowMetricsPointCompat
import dagger.android.support.DaggerDialogFragment

abstract class BaseDialogFragment<B : ViewDataBinding>
constructor(@LayoutRes private val layoutId: Int) : DaggerDialogFragment() {

    protected lateinit var returnKey: String

    companion object {
        // Dialog 의 결과값을 리턴받는 Key 값
        const val DIALOG_RETURN_VALUE_OK = "OK"
        const val DIALOG_RETURN_VALUE_CANCEL = "CANCEL"
    }

    lateinit var mContext: Context

    protected lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutId, null, false)
        binding.lifecycleOwner = this
        mContext = inflater.context
        initViewCreated(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val windowsManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val deviceWidth = windowsManager.currentWindowMetricsPointCompat().x
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    abstract fun initViewCreated(inflater: LayoutInflater)

    abstract fun init()

    protected fun showToast(msg: String) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}