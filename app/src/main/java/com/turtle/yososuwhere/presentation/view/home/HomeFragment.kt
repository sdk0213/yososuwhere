package com.turtle.yososuwhere.presentation.view.home

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.turtle.yososuwhere.R
import com.turtle.yososuwhere.databinding.FragmentHomeBinding
import com.turtle.yososuwhere.presentation.android.shard_pref.SharedPrefUtil
import com.turtle.yososuwhere.presentation.view.base.BaseFragment
import javax.inject.Inject


class HomeFragment :
    BaseFragment<HomeViewModel, FragmentHomeBinding>(R.layout.fragment_home) {

    @Inject
    lateinit var sharedPrefUtil: SharedPrefUtil

    private val args: HomeFragmentArgs? by navArgs()

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

        args?.let {
            (0..3).forEach { count ->
                when (count) {
                    0 -> {
                        binding.tvHomeHasStockColorGreen.text = it.yososuCount.split("/")[count]
                    }
                    1 -> {
                        binding.tvHomeHasStockColorYellow.text = it.yososuCount.split("/")[count]
                    }
                    2 -> {
                        binding.tvHomeHasStockColorRed.text = it.yososuCount.split("/")[count]
                    }
                    3 -> {
                        binding.tvHomeHasStockColorGray.text = it.yososuCount.split("/")[count]
                    }
                }
            }
        }
    }

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
