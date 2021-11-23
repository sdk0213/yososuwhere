package com.turtle.yososuwhere.presentation.view.home

import com.turtle.yososuwhere.R
import com.turtle.yososuwhere.databinding.FragmentHomeBinding
import com.turtle.yososuwhere.presentation.utilities.EventObserver
import com.turtle.yososuwhere.presentation.view.base.BaseFragment

class HomeFragment :
    BaseFragment<HomeViewModel, FragmentHomeBinding>(R.layout.fragment_home) {

    private val yososuStationAdapter: HomeYososuStationAdapter by lazy {
        HomeYososuStationAdapter(mContext)
    }

    override fun init() {
        view()
        viewModel()
        listener()
        observer()

    }

    private fun view() {
        showPopUpMessage(
            "데이터 출처 : 공공데이터 포털(공공데이터활용지원센터)\n" +
                    "해당 앱에서 제공하는 정보는 부정확할수 있으며 자세한 사항은 해당 주유소에 직접 문의하시기 바랍니다.\n" +
                    "해당 앱에서 제공받은 정보로 발생하는 불이익은 사용자에게 있습니다.\n" +
                    "데이터는 하루 두번 업데이트 됩니다.\n" +
                    "(12시 기준의 데이터를 14시에 업데이트)\n" +
                    "(18시 기준의 데이터를 20시에 업데이트)"
        )

    }

    private fun viewModel() {
        binding.viewModel = viewModel
        binding.recyclerviewHomeYososulist.adapter = yososuStationAdapter
    }

    private fun listener() {

    }

    private fun observer() {

        viewModel.errorMessage.observe(this@HomeFragment, EventObserver {
            showToast(it)
        })

        viewModel.yososuStationEntityList.observe(this@HomeFragment) { list ->
            yososuStationAdapter.submitList(list)
        }
    }

}
