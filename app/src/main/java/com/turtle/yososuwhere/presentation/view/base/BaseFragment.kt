package com.turtle.yososuwhere.presentation.view.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.turtle.yososuwhere.presentation.android.di.factory.ViewModelFactory
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Inject


abstract class BaseFragment<T : BaseViewModel, B : ViewDataBinding>
constructor(@LayoutRes private val layoutId: Int) : DaggerFragment() {

    companion object {
        // Dialog 의 결과값을 리턴받는 Key 값
        const val DIALOG_RETURN_VALUE_OK = "OK"
        const val DIALOG_RETURN_VALUE_CANCEL = "CANCEL"
    }

    var compositeDisposable = CompositeDisposable()

    protected val handler by lazy {
        Handler(Looper.getMainLooper())
    }

    lateinit var mContext: Context

    protected lateinit var binding: B

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected val viewModel: T
        get() {
            val types: Array<Type> =
                (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
            return ViewModelProvider(this, viewModelFactory).get(types[0] as Class<T>)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutId, null, false)
        binding.lifecycleOwner = this
        mContext = inflater.context
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }

    abstract fun init()

    protected fun showToast(msg: String) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}