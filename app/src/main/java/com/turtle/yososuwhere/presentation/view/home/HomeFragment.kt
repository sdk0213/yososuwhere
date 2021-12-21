package com.turtle.yososuwhere.presentation.view.home

import androidx.navigation.fragment.findNavController
import com.turtle.yososuwhere.R
import com.turtle.yososuwhere.databinding.FragmentHomeBinding
import com.turtle.yososuwhere.presentation.android.shard_pref.SharedPrefUtil
import com.turtle.yososuwhere.presentation.view.base.BaseFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


class HomeFragment :
    BaseFragment<HomeViewModel, FragmentHomeBinding>(R.layout.fragment_home) {

    @Inject
    lateinit var sharedPrefUtil: SharedPrefUtil

    private val yososuStationAdapter: HomeYososuStationPagingAdapter by lazy {
        HomeYososuStationPagingAdapter(
            mContext = mContext
        )
    }

    override fun init() {
        view()
        viewModel()
        listener()
        observer()
    }

    private fun view() {
        binding.recyclerviewHomeYososulist.adapter = yososuStationAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter()
        )

        binding.topAppBar.apply {
            setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24)
            setNavigationIconTint(0xFFFFFFFF.toInt())
            setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun viewModel() {
        binding.viewModel = viewModel
        viewModel.getYososuStationByPaging()
    }

    private fun listener() {

    }

    private fun observer() {

        viewModel.yososuStationPagingData.observe(this@HomeFragment) { pagingData ->
            yososuStationAdapter.submitData(lifecycle, pagingData)
        }
    }

}
