package com.turtle.yososuwhere.presentation.view.yososu_map

import android.Manifest
import android.content.Intent
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

    @Inject
    lateinit var sharedPrefUtil: SharedPrefUtil

    private var trackingMode = LocationTrackingMode.None
    private val mapFragment: MapFragment by lazy {
        childFragmentManager.findFragmentById(R.id.fragment_naver_map) as MapFragment
    }

    private lateinit var mMap: NaverMap

    private val markers = mutableListOf<Marker>()

    private lateinit var locationSource: FusedLocationSource

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
                markers.find {
                    it.position.let { latLng ->
                        latLng.latitude == yososuStation.lat || latLng.longitude == yososuStation.lon
                    }
                }?.let {
                    moveToNaverMapMarker(
                        LatLng(
                            it.position.latitude,
                            it.position.longitude
                        )
                    )
                }
            },
            sharedPrefUtil = sharedPrefUtil
        )
    }

    private var viewList = mutableListOf<YososuStation>()

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
                        trackingMode = if (tedPermissionResult.isGranted) {
                            LocationTrackingMode.Follow
                        } else {
                            LocationTrackingMode.None
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
    }

    private fun listener() {

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_refresh -> {
                    requestPermission()
                    true
                }
                R.id.item_map_filter -> {
                    sharedPrefUtil.useFilterByHasStock = !sharedPrefUtil.useFilterByHasStock
                    binding.topAppBar.menu.findItem(R.id.item_map_filter)
                        .setIcon(if (sharedPrefUtil.useFilterByHasStock) R.drawable.ic_baseline_filter_list_24 else R.drawable.ic_baseline_filter_list_off_24)
                    showToast(if (sharedPrefUtil.useFilterByHasStock) "요소수 없는곳 제외" else "전부 표시")
                    if (sharedPrefUtil.useFilterByHasStock) yososuStationAdapter.filterByHasYososu() else yososuStationAdapter.noFilter()
                    refreshMarkers()
                    true
                }
                R.id.item_yososu_list -> {
                    findNavController().navigate(
                        YososuMapFragmentDirections.actionMapFragmentToHomeFragment()
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
            mMap.locationTrackingMode = trackingMode
        }

    }

    private fun observer() {

        viewModel.errorMessage.observe(this@YososuMapFragment, EventObserver {
            showToast(it)
        })

        viewModel.myLocation.observe(this@YososuMapFragment) { location ->
            Timber.d(location.toString())
        }

        viewModel.yososuStationList.observe(this@YososuMapFragment, EventObserver { yososuStationList ->
            // View 에서 관리가능하도록 리스트 저장
            viewList.addAll(yososuStationList)
            yososuStationAdapter.submit(viewList)
            makeMarkerWithFiltered(viewList)
            markers.forEach { it.map = mMap }
        })

        viewModel.cannotGetLocation.observe(this@YososuMapFragment, EventObserver { noLocation ->
            if (noLocation) {
                showToast("위치 정보를 가져올수 없습니다.")
            }
        })
    }

    override fun onMapReady(naverMap: NaverMap) {
        mMap = naverMap
        viewModel.getYososuStation()
        locationSource = FusedLocationSource(this, 1000)
        mMap.locationSource = locationSource
        mMap.locationTrackingMode = trackingMode
        naverMap.addOnLocationChangeListener {
            viewModel.currentLocation(it)
        }

        mMap.addOnCameraIdleListener {
            markers.forEach { marker ->
                marker.isVisible = mMap.contentBounds.contains(marker.position)
            }
        }
    }

    private fun moveToNaverMapMarker(latLng: LatLng) {
        mMap?.let {
            it.moveCamera(
                CameraUpdate
                    .scrollTo(latLng) // 이동 위치
                    .animate(CameraAnimation.Fly) // 이동 애니메이션 처리
                    .finishCallback {
                        // 13.5 로 줌
                        mMap.moveCamera(CameraUpdate.zoomTo(14.0))
                    }
            )
        }
    }

    private fun refreshMarkers() {
        markers.forEach { it.map = null }
        markers.clear()
        makeMarkerWithFiltered(viewList)
        markers.forEach { it.map = mMap }
    }

    private fun makeMarkerWithFiltered(yososuList: List<YososuStation>) {
        val filteredList =
            if (sharedPrefUtil.useFilterByHasStock) yososuList.filter { it.stock != 0L } else yososuList
        filteredList.forEach { yososuStation ->
            markers.add(
                // 마커 그리기 및 뷰 붙히기
                Marker().apply {
                    position = LatLng(yososuStation.lat, yososuStation.lon)
                    icon = OverlayImage.fromView(
                        (layoutInflater.inflate(
                            R.layout.item_marker,
                            null
                        ) as ConstraintLayout).apply {
                            (this.getViewById(R.id.tv_marker_gas_station_name) as TextView).text =
                                yososuStation.name
                            (this.getViewById(R.id.tv_marker_stock) as TextView).text =
                                "${getString(R.string.map_marker_stock)} : ${yososuStation.stock}"
                            if(yososuStation.stock == 0L){
                                (this.getViewById(R.id.tv_marker_gas_station_name) as TextView).setTextColor(0xFFFFFFFF.toInt())
                                (this.getViewById(R.id.tv_marker_gas_station_name) as TextView).setBackgroundColor(0xFF000000.toInt())
                            } else {
                                (this.getViewById(R.id.tv_marker_gas_station_name) as TextView).setTextColor(0xFFFFFFFF.toInt())
                                (this.getViewById(R.id.tv_marker_gas_station_name) as TextView).setBackgroundColor(0xFFFF6E40.toInt())
                            }
                            (this.getViewById(R.id.tv_marker_cost) as TextView).text =
                                "${getString(R.string.map_marker_cost)} : ${yososuStation.cost}원"
                            onClickListener = Overlay.OnClickListener {
                                val latLng = LatLng(
                                    yososuStation.lat,
                                    yososuStation.lon
                                )
                                moveToNaverMapMarker(latLng)
                                binding.recyclerviewYososuMapYososulist.scrollToPosition(
                                    yososuStationAdapter.currentList.indexOf(yososuStation) ?: 0
                                )
                                true
                            }
                            isHideCollidedMarkers = true
                        }
                    )
                }
            )
        }
    }

}
