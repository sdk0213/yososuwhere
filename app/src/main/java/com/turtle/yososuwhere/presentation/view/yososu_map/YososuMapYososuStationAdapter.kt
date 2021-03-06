package com.turtle.yososuwhere.presentation.view.yososu_map

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.naver.maps.geometry.LatLng
import com.turtle.yososuwhere.R
import com.turtle.yososuwhere.databinding.ListItemMapYososuBinding
import com.turtle.yososuwhere.domain.model.YososuStation
import com.turtle.yososuwhere.presentation.android.shard_pref.SharedPrefUtil
import com.turtle.yososuwhere.presentation.utilities.extensions.convertToDateDetail
import com.turtle.yososuwhere.presentation.utilities.extensions.getCountHourAndtime
import java.util.*


class MapYososuStationAdapter constructor(
    private val mContext: Context,
    private val moveToMarker: (YososuStation) -> (Unit),
    private val sharedPrefUtil: SharedPrefUtil
) : ListAdapter<YososuStation, MapYososuStationAdapter.HomeYososuStationViewHolder>(
    YososuStationDiffCallback()
) {

    private var modelDataList = mutableListOf<YososuStation>()
    private var viewDataList = mutableListOf<YososuStation>()

    private var viewPosition = mutableMapOf<LatLng, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeYososuStationViewHolder {
        return HomeYososuStationViewHolder(
            ListItemMapYososuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeYososuStationViewHolder, position: Int) {
        viewPosition[LatLng(getItem(position).lat, getItem(position).lon)] = position
        holder.bind(getItem(position))
    }

    fun submit(list: List<YososuStation>) {
        modelDataList = list.toMutableList()
        // ?????? ??????????????? ?????? ????????? ??????
        if (sharedPrefUtil.useFilterByHasStock) {
            filterByHasYososu()
        } else {
            noFilter()
        }
    }

    // ?????? ??????
    fun noFilter() {
        viewDataList = modelDataList
        submitList(viewDataList)
    }

    // ????????? ????????? ????????????
    fun filterByHasYososu() {
        viewDataList = modelDataList
        submitList(viewDataList.filter {
            it.stock != 0L
        })
    }

    fun getYososuViewPosition(): Map<LatLng, Int> {
        return viewPosition.toMutableMap()
    }

    inner class HomeYososuStationViewHolder(
        private val binding: ListItemMapYososuBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: YososuStation) {
            binding.apply {
                btnListItemHomeGasStationTel.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:${btnListItemHomeGasStationTel.text}")
                    }
                    startActivity(mContext, intent, null)
                }
                val hasYososu = item.stock == 0L
                if (hasYososu) {
                    tvListItemHomeGasStationYososuStock.setTextColor(0xFF888888.toInt())
                    tvListItemHomeGasStationName.setTextColor(0xFFFFFFFF.toInt())
                    tvListItemHomeGasStationName.setBackgroundColor(0xFF000000.toInt())
                } else {
                    tvListItemHomeGasStationYososuStock.setTextColor(0xFF000000.toInt())
                    tvListItemHomeGasStationName.setTextColor(0xFFFFFFFF.toInt())
                    tvListItemHomeGasStationName.setBackgroundColor(0xFFFF6E40.toInt())
                }
                tvListItemHomeGasStationAddr.text = item.addr
                tvListItemHomeGasStationName.text = item.name
                btnListItemHomeGasStationTel.text = item.tel
                tvListItemHomeGasStationHoursOfOperation.text = "???????????? : ${item.operationTime}"
                tvListItemHomeGasStationYososuStock.text =
                    if (hasYososu) "????????? ??????" else "?????? : ${item.stock}"
                tvListItemHomeGasStationYososuCost.text = "?????? : ${item.cost}???"
                tvListItemHomeGasStationYososuStandardTime.text =
                    "?????? ???????????? ${Date().getCountHourAndtime(item.dataStandard.convertToDateDetail())} ??? ?????????????????????"

                imageViewListItemHomeGasStationColor.apply {
                    when (item.color) {
                        "GREEN" -> {
                            setImageResource(R.drawable.flaticon_com_ic_traffic_lights_green)
                        }
                        "YELLOW" -> {
                            setImageResource(R.drawable.flaticon_com_ic_traffic_lights_yellow)
                        }
                        "RED" -> {
                            setImageResource(R.drawable.flaticon_com_ic_traffic_lights_red)
                        }
                        else -> {
                            setImageResource(R.drawable.flaticon_com_ic_traffic_lights_gray)
                        }
                    }
                }

                cardViewListHome.setOnClickListener {
                    val clipboard: ClipboardManager =
                        mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("", item.addr)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(mContext, "????????? ????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
                    moveToMarker(item)
                }

                executePendingBindings()
            }
        }

    }

}