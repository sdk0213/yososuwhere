package com.turtle.yososuwhere.presentation.view.yososu_map

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.navArgs
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
import com.turtle.yososuwhere.presentation.android.shard_pref.SharedPrefUtil
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
                "현재 위치를 기준으로 가장 가까운 주유소를 찾으려면 위치권한이 필요합니다.\n" +
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
            clipboardSave = { yososuStation ->
                val clipboard: ClipboardManager =
                    requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("", "${yososuStation.addr}")
                clipboard.setPrimaryClip(clip)
                showToast("주유소 주소를 복사하였습니다.")
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

    private val args: YososuMapFragmentArgs by navArgs()

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
                        mapFragment.getMapAsync(this)
                    },
                    {
                        showToast("ERROR")
                    }
                )
        )
    }

    private fun view() {
        binding.recyclerviewYososuMapYososulist.adapter = yososuStationAdapter

    }

    private fun viewModel() {
        binding.viewModel = viewModel
        if (!args.yososuStations.isNullOrEmpty()) {
            viewModel.setYososuList(args.yososuStations)
        }
    }

    private fun listener() {
        binding.btnYososuMapMyLocation.setOnClickListener {
            mMap.locationTrackingMode = trackingMode
        }
    }

    private fun observer() {
        viewModel.myLocation.observe(this@YososuMapFragment) { location ->
            Timber.tag("dksung").d(location.toString())
        }

        viewModel.yososuList.observe(this@YososuMapFragment) { yososuList ->
            yososuStationAdapter.submit(yososuList)
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
                                (this.getViewById(R.id.tv_marker_cost) as TextView).text =
                                    "${getString(R.string.map_marker_cost)} : ${yososuStation.cost}원"
                                onClickListener = Overlay.OnClickListener {
                                    val latLng = LatLng(
                                        yososuStation.lat,
                                        yososuStation.lon
                                    )
                                    moveToNaverMapMarker(latLng)
                                    Timber.tag("dksung").d("yososuStation : ${yososuStation.name}")
                                    Timber.tag("dksung")
                                        .d("${yososuStationAdapter.currentList.indexOf(yososuStation)}")
                                    for (i in 0 until yososuStationAdapter.currentList.size) {
                                        Timber.tag("dksung")
                                            .d("$i = ${yososuStationAdapter.currentList[i].name}")
                                    }
                                    binding.recyclerviewYososuMapYososulist.smoothScrollToPosition(
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

    override fun onMapReady(naverMap: NaverMap) {
        mMap = naverMap
        locationSource = FusedLocationSource(this, 1000)
        mMap.locationSource = locationSource
        mMap.locationTrackingMode = trackingMode
        naverMap.addOnLocationChangeListener {
            viewModel.currentLocation(it)
        }
        markers.forEach {
            it.map = mMap
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
                        mMap.moveCamera(CameraUpdate.zoomTo(11.0))
                    }
            )
        }
    }

}
