package com.turtle.yososuwhere.presentation.view.map

import android.Manifest
import com.gun0912.tedpermission.TedPermissionResult
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.tedpark.tedpermission.rx2.TedRxPermission
import com.turtle.yososuwhere.R
import com.turtle.yososuwhere.databinding.FragmentMapBinding
import com.turtle.yososuwhere.presentation.view.base.BaseFragment
import io.reactivex.Single
import timber.log.Timber

class MapFragment :
    BaseFragment<MapViewModel, FragmentMapBinding>(R.layout.fragment_map), OnMapReadyCallback {

    private var trackingMode = LocationTrackingMode.None
    private val mapFragment: MapFragment by lazy {
        childFragmentManager.findFragmentById(R.id.fragment_naver_map) as MapFragment
    }

    private lateinit var mMap: NaverMap

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

    }

    private fun viewModel() {
        binding.viewModel = viewModel
    }

    private fun listener() {
    }

    private fun observer() {
        viewModel.myLocation.observe(this@MapFragment) { location ->
            Timber.tag("dksung").d(location.toString())
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
    }

}
