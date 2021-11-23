package com.turtle.yososuwhere.presentation.view.home

import android.Manifest
import com.gun0912.tedpermission.TedPermissionResult
import com.tedpark.tedpermission.rx2.TedRxPermission
import com.turtle.yososuwhere.R
import com.turtle.yososuwhere.databinding.FragmentHomeBinding
import com.turtle.yososuwhere.presentation.utilities.EventObserver
import com.turtle.yososuwhere.presentation.view.base.BaseFragment
import io.reactivex.Single
import timber.log.Timber

class HomeFragment :
    BaseFragment<HomeViewModel, FragmentHomeBinding>(R.layout.fragment_home) {

    private val permissionRx: Single<TedPermissionResult> by lazy {
        TedRxPermission.create().apply {
            setDeniedTitle("위치 권한 필요")
            setDeniedMessage(
                "현재 위치를 기준으로 가장 가까운 주유소를 찾으려면 위치권한이 필요합니다.\n" +
                        "[설정] > [권한] > [위치]를 [이번만 허용 또는 앱 사용중에만 허용]으로 변경해주세요."
            )
            setPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }.request()
    }

    private val yososuStationAdapter: HomeYososuStationAdapter by lazy {
        HomeYososuStationAdapter(mContext)
    }

    override fun init() {
        view()
        viewModel()
        listener()
        observer()
        requestPermission()
    }

    private fun requestPermission() {
        compositeDisposable.add(
            permissionRx
                .subscribe(
                    { tedPermissionResult ->
                        if (tedPermissionResult.isGranted) {
                            Timber.d("권한이 허용됨")
                            viewModel.getYososuStation()
                        }
                    },
                    {
                        showToast("ERROR")
                    }
                )
        )
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
        binding.btnHomeRefresh.setOnClickListener {
            requestPermission()
        }
    }

    private fun observer() {

        viewModel.errorMessage.observe(this@HomeFragment, EventObserver {
            showToast(it)
        })

        viewModel.yososuStationEntityList.observe(this@HomeFragment) { list ->
            yososuStationAdapter.submitList(list)
            binding.recyclerviewHomeYososulist.smoothScrollToPosition(0)
        }

        viewModel.cannotGetLocation.observe(this@HomeFragment, EventObserver{ noLocation ->
            if(noLocation){
                binding.tvHomeGps.text = "위치 정보를 가져올수 없습니다."
            }
        })
    }

}
