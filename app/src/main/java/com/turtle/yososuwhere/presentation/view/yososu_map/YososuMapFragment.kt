package com.turtle.yososuwhere.presentation.view.yososu_map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.gun0912.tedpermission.TedPermissionResult
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.tedpark.tedpermission.rx2.TedRxPermission
import com.turtle.yososuwhere.R
import com.turtle.yososuwhere.databinding.FragmentYososuMapBinding
import com.turtle.yososuwhere.domain.model.YososuStation
import com.turtle.yososuwhere.presentation.android.shard_pref.SharedPrefUtil
import com.turtle.yososuwhere.presentation.utilities.EventObserver
import com.turtle.yososuwhere.presentation.view.base.BaseFragment
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class YososuMapFragment :
    BaseFragment<YososuMapViewModel, FragmentYososuMapBinding>(R.layout.fragment_yososu_map),
    OnMapReadyCallback {

    companion object {
        private const val LOADING_MAX = 100
    }

    @Inject
    lateinit var sharedPrefUtil: SharedPrefUtil

    private val mapFragment: MapFragment by lazy {
        childFragmentManager.findFragmentById(R.id.fragment_naver_map) as MapFragment
    }

    private var mMap: NaverMap? = null

    private val markers = mutableListOf<Marker>()

    private val permissionRx: Single<TedPermissionResult> by lazy {
        TedRxPermission.create().apply {
            setDeniedTitle("위치 권한 필요")
            setDeniedMessage(
                "현재 위치를 가져오려면 위치권한이 필요합니다.\n" +
                        "[설정] > [권한] > [위치]를 [이번만 허용 또는 앱 사용중에만 허용]으로 변경해주세요."
            )
            setPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }.request()
    }

    private val yososuStationAdapter: MapYososuStationAdapter by lazy {
        MapYososuStationAdapter(
            mContext = mContext,
            moveToMarker = { yososuStation ->
                moveCameraToLatLng(
                    LatLng(
                        yososuStation.lat,
                        yososuStation.lon
                    )
                )
            },
            sharedPrefUtil = sharedPrefUtil
        )
    }

    // View 에서 관리가능하도록 리스트 저장
    private var viewYososuStationList = mutableListOf<YososuStation>()

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
                        mMap?.let {
                            it.locationTrackingMode = if (tedPermissionResult.isGranted) {
                                LocationTrackingMode.Follow
                            } else {
                                LocationTrackingMode.None
                            }
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

        mapFragment.getMapAsync(this)
        binding.recyclerviewYososuMapYososulist.adapter = yososuStationAdapter
        binding.topAppBar.menu.findItem(R.id.item_map_filter)
            .setIcon(if (sharedPrefUtil.useFilterByHasStock) R.drawable.ic_baseline_filter_list_24 else R.drawable.ic_baseline_filter_list_off_24)
    }

    private fun viewModel() {
        binding.viewModel = viewModel
        binding.progressBar.show()
        viewModel.getYososuStation()
    }

    private fun listener() {

        binding.topAppBar.setOnMenuItemClickListener {
            if (binding.progressBar.isShown) {
                showToast(getString(R.string.map_toast_message_is_loading))
                return@setOnMenuItemClickListener false
            }
            when (it.itemId) {
                R.id.item_refresh -> {
                    viewYososuStationList.clear()
                    binding.progressBar.show()
                    binding.progressBar.progress = 0
                    viewModel.getYososuStation()
                    true
                }
                R.id.item_map_filter -> {
                    sharedPrefUtil.useFilterByHasStock = !sharedPrefUtil.useFilterByHasStock
                    binding.topAppBar.menu.findItem(R.id.item_map_filter)
                        .setIcon(if (sharedPrefUtil.useFilterByHasStock) R.drawable.ic_baseline_filter_list_24 else R.drawable.ic_baseline_filter_list_off_24)
                    if (sharedPrefUtil.useFilterByHasStock) yososuStationAdapter.filterByHasYososu() else yososuStationAdapter.noFilter()
                    mMap?.let {
                        // 재 생성 비용문제로 좌표를 보이지 않는곳으로 이동처리
                        //      - 20개보다 적은 데이터가 지도에 있을경우 필터를 하여도 기존 마커가 삭제되지 않는 현상에 대한 개선
                        markers.forEach { marker ->
                            marker.position = LatLng(0.0, 0.0)
                        }
                    }
                    showToast(if (sharedPrefUtil.useFilterByHasStock) "요소수 없는곳 제외" else "전부 표시")
                    refreshMarkers()
                    true
                }
                R.id.item_yososu_list -> {
                    findNavController().navigate(
                        YososuMapFragmentDirections.actionMapFragmentToHomeFragment(
                            viewModel.countStationColor()
                        )
                    )
                    true
                }
                R.id.item_qna -> {
                    findNavController().navigate(
                        YososuMapFragmentDirections.actionMapFragmentToQnaFragment()
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

        binding.btnYososuMapMyLocation.setOnClickListener {
            requestPermission()
        }

    }

    private fun observer() {

        viewModel.errorMessage.observe(this@YososuMapFragment, EventObserver {
            binding.progressBar.progress = 0
            binding.progressBar.hide()
            showToast(it)
        })

        viewModel.myLocation.observe(this@YososuMapFragment) { location ->
            Timber.d(location.toString())
        }

        viewModel.yososuStationList.observe(
            this@YososuMapFragment,
            EventObserver { yososuStationList ->
                // View 에서 관리가능하도록 리스트 저장
                viewYososuStationList.addAll(yososuStationList)
                yososuStationAdapter.submit(viewYososuStationList)
            }
        )

        viewModel.loadingPercent.observe(
            this@YososuMapFragment,
            EventObserver { percent ->
                binding.progressBar.progress = percent
                if (percent == LOADING_MAX) {
                    binding.progressBar.hide()
                    refreshMarkers()
                    showToast("요소수 정보를 업데이트하였습니다.")
                }
            }
        )
    }

    override fun onMapReady(naverMap: NaverMap) {
        mMap = naverMap
        mMap!!.locationSource = FusedLocationSource(this, 1000)
        mMap!!.locationTrackingMode =
            if (mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationTrackingMode.Follow
            } else {
                LocationTrackingMode.None
            }
        mMap!!.addOnLocationChangeListener {
            viewModel.currentLocation(it)
        }

        // 마커를 20 개만 고정적으로 생성 후 이후 해당 객체(마커)의 내용만 교체를 진행
        (1..20).forEach { _ ->
            markers.add(
                Marker().apply {
                    position = LatLng(0.0, 0.0)
                    this.map = mMap!!
                }
            )
        }

        // 카메라가 멈췄을때
        mMap!!.addOnCameraIdleListener {
            // 마커를 재생성
            refreshMarkers()
        }
    }

    private fun moveCameraToLatLng(latLng: LatLng) {
        mMap?.let {
            it.moveCamera(
                CameraUpdate
                    .scrollTo(latLng) // 이동 위치
                    .animate(CameraAnimation.Fly) // 이동 애니메이션 처리
                    .finishCallback {
                        // 13.5 로 줌
                        mMap?.moveCamera(CameraUpdate.zoomTo(14.0))
                    }
            )
        }
    }

    private fun refreshMarkers() {
        // 요소수 없는곳 필터
        val filteredYososuStationList =
            if (sharedPrefUtil.useFilterByHasStock) viewYososuStationList.filter { it.stock != 0L } else viewYososuStationList

        // 사용자에게 표시되는 맵의 포함된 위치의 주유소만 마커로 표시하며 이는 최대 20개를 넘지 않음
        var makeMarkerCount = 0
        for (yososuStation in filteredYososuStationList) {
            if (mMap!!.contentBounds.contains(LatLng(yososuStation.lat, yososuStation.lon))) {
                markers[makeMarkerCount].apply {
                    // 위치
                    position = LatLng(yososuStation.lat, yososuStation.lon)
                    // 아이콘 (커스텀 사용)
                    icon = OverlayImage.fromView(
                        makeCustomMarkerView(yososuStation)
                    )
                    // 클릭하였을떄
                    onClickListener = Overlay.OnClickListener {
                        val latLng = LatLng(
                            yososuStation.lat,
                            yososuStation.lon
                        )
                        moveCameraToLatLng(latLng)
                        binding.recyclerviewYososuMapYososulist.scrollToPosition(
                            yososuStationAdapter.currentList.indexOf(yososuStation) ?: 0
                        )
                        true
                    }
                }
                if (++makeMarkerCount == 20) { // 생성가능한 마커의 개수(20개) 만큼만 변경
                    break
                }
            }
        }
    }

    private fun makeCustomMarkerView(yososuStation: YososuStation) = (layoutInflater.inflate(
        R.layout.item_marker,
        null
    ) as ConstraintLayout).apply {
        (this.getViewById(R.id.tv_marker_gas_station_name) as TextView).text =
            yososuStation.name
        (this.getViewById(R.id.tv_marker_stock) as TextView).text =
            "${getString(R.string.map_marker_stock)} : ${yososuStation.stock}"
        if (yososuStation.stock == 0L) {
            (this.getViewById(R.id.tv_marker_gas_station_name) as TextView).setTextColor(0xFFFFFFFF.toInt())
            (this.getViewById(R.id.tv_marker_gas_station_name) as TextView).setBackgroundColor(
                0xFF000000.toInt()
            )
        } else {
            (this.getViewById(R.id.tv_marker_gas_station_name) as TextView).setTextColor(0xFFFFFFFF.toInt())
            (this.getViewById(R.id.tv_marker_gas_station_name) as TextView).setBackgroundColor(
                0xFFFF6E40.toInt()
            )
        }
        (this.getViewById(R.id.tv_marker_cost) as TextView).text =
            if (yososuStation.cost == "undefined") "${getString(R.string.map_marker_cost)} : 미정" else "${
                getString(
                    R.string.map_marker_cost
                )
            } : ${yososuStation.cost}원"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mMap?.let {
            markers.forEach { marker ->
                marker.map = null
            }
            markers.clear()
        }
    }
}
