package com.turtle.yososuwhere.presentation.view.home

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.gun0912.tedpermission.TedPermissionResult
import com.tedpark.tedpermission.rx2.TedRxPermission
import com.turtle.yososuwhere.R
import com.turtle.yososuwhere.databinding.FragmentHomeBinding
import com.turtle.yososuwhere.domain.model.YososuStations
import com.turtle.yososuwhere.presentation.android.shard_pref.SharedPrefUtil
import com.turtle.yososuwhere.presentation.utilities.EventObserver
import com.turtle.yososuwhere.presentation.view.base.BaseFragment
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject


class HomeFragment :
    BaseFragment<HomeViewModel, FragmentHomeBinding>(R.layout.fragment_home) {

    @Inject
    lateinit var sharedPrefUtil: SharedPrefUtil

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
        HomeYososuStationAdapter(
            mContext = mContext,
            clipboardSave = {
                val clipboard: ClipboardManager =
                    requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("", "${it.addr}")
                clipboard.setPrimaryClip(clip)
                showToast("주유소 주소를 복사하였습니다.")
            },
            sharedPrefUtil = sharedPrefUtil
        )
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
                        } else {
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
            "데이터 제공기관 : 환경부(교통환경과)\n\n" +
                    "전국의 중점 유통 주유소의 요소수 재고 현황이며, 요소수 중점유통 주유소에서 2시간 간격으로 입력하는 데이터를 5분 단위로 업데이트하여 제공하여 실제 재고 현황과 일부 차이가 있을 수 있습니다.\n\n" +
                    "공개되는 요소수 가격은 기본적으로 벌크 요소수 가격이며, 페트 요소수 가격은 표시된 가격과 다를 수 있습니다.\n" +
                    "벌크 요소수가 매진 되었을 경우, 페트 요소수 가격으로 업데이트 됩니다.\n\n\n" +
                    "(해당 앱에서 제공하는 정보는 부정확할수 있으며 자세한 사항은 해당 주유소에 직접 문의하시기 바랍니다.)\n" +
                    "(해당 앱에서 제공받은 정보로 발생하는 불이익은 사용자에게 있습니다.)"
        )
        binding.topAppBar.menu.findItem(R.id.item_home_filter)
            .setIcon(if (sharedPrefUtil.useFilterByHasStock) R.drawable.ic_baseline_filter_list_24 else R.drawable.ic_baseline_filter_list_off_24)
        binding.recyclerviewHomeYososulist.adapter = yososuStationAdapter
    }

    private fun viewModel() {
        binding.viewModel = viewModel
    }

    private fun listener() {

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_refresh -> {
                    requestPermission()
                    true
                }
                R.id.item_home_filter -> {
                    sharedPrefUtil.useFilterByHasStock = !sharedPrefUtil.useFilterByHasStock
                    binding.topAppBar.menu.findItem(R.id.item_home_filter)
                        .setIcon(if (sharedPrefUtil.useFilterByHasStock) R.drawable.ic_baseline_filter_list_24 else R.drawable.ic_baseline_filter_list_off_24)
                    if (sharedPrefUtil.useFilterByHasStock) yososuStationAdapter.filterByHasYososu() else yososuStationAdapter.noFilter()
                    showToast(if (sharedPrefUtil.useFilterByHasStock) "요소수 없는곳 제외" else "전부 표시")
                    true
                }
                R.id.item_search_map -> {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToMapFragment(
                            YososuStations().apply {
                                viewModel.yososuStationEntityList.value?.peekContent()
                                    ?.let { yososuStations -> addAll(yososuStations) }
                            }
                        )
                    )
                    true
                }
                R.id.item_qna -> {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToQnaFragment()
                    )
                    true

                }
                R.id.item_open_source_license -> {
                    OssLicensesMenuActivity.setActivityTitle("오픈소스 라이선스")
                    startActivity(
                        Intent(
                            mContext,
                            OssLicensesMenuActivity::class.java
                        )
                    )
                    true
                }
                else -> {
                    true
                }
            }
        }

    }

    private fun observer() {

        viewModel.errorMessage.observe(this@HomeFragment, EventObserver {
            showToast(it)
        })

        viewModel.yososuStationEntityList.observe(this@HomeFragment, EventObserver { list ->
            showToast("주유소 요소수 정보를 업데이트하였습니다")
            binding.tvHomeHasStockColorGreen.text = list.count { it.color == "GREEN" }.toString()
            binding.tvHomeHasStockColorYellow.text = list.count { it.color == "YELLOW" }.toString()
            binding.tvHomeHasStockColorRed.text = list.count { it.color == "RED" }.toString()
            binding.tvHomeHasStockColorGray.text = list.count { it.color == "GRAY" }.toString()
            yososuStationAdapter.submit(list)
            handler.postDelayed({
                binding.recyclerviewHomeYososulist.smoothScrollToPosition(0)
            }, 500)

        })

        viewModel.cannotGetLocation.observe(this@HomeFragment, EventObserver { noLocation ->
            if (noLocation) {
                binding.tvHomeGps.text = "위치 정보를 가져올수 없습니다."
            }
        })
    }

}
