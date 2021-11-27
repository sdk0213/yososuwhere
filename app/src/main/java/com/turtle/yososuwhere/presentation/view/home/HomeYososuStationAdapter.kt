package com.turtle.yososuwhere.presentation.view.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.turtle.yososuwhere.R
import com.turtle.yososuwhere.databinding.ListItemHomeYososuBinding
import com.turtle.yososuwhere.domain.model.YososuStation
import com.turtle.yososuwhere.presentation.utilities.extensions.convertToDateDetail
import com.turtle.yososuwhere.presentation.utilities.extensions.getCountHourAndtime
import java.util.*


class HomeYososuStationAdapter constructor(
    private val mContext: Context,
    private val clipboardSave: (YososuStation) -> (Unit)
) : ListAdapter<YososuStation, HomeYososuStationAdapter.HomeYososuStationViewHolder>(
    HomeYososuStationDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeYososuStationViewHolder {
        return HomeYososuStationViewHolder(
            ListItemHomeYososuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeYososuStationViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class HomeYososuStationViewHolder(
        private val binding: ListItemHomeYososuBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: YososuStation, position: Int) {
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
                } else {
                    tvListItemHomeGasStationYososuStock.setTextColor(0xFF000000.toInt())
                }
                tvListItemHomeGasStationNumber.text = "(${position + 1}/${itemCount})"
                tvListItemHomeGasStationAddr.text = item.addr
                tvListItemHomeGasStationName.text = item.name
                btnListItemHomeGasStationTel.text = item.tel
                tvListItemHomeGasStationHoursOfOperation.text = "영업시간 : ${item.operationTime}"
                tvListItemHomeGasStationYososuStock.text =
                    if (hasYososu) "요소수 없음" else "재고 : ${item.stock}"
                tvListItemHomeGasStationYososuCost.text = "가격 : ${item.cost}원"
                tvListItemHomeGasStationYososuStandardTime.text =
                    "해당 주유소의 ${Date().getCountHourAndtime(item.dataStandard.convertToDateDetail())} 전 재고정보입니다"

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
                    clipboardSave(item)
                }

                executePendingBindings()
            }
        }

    }

}

class HomeYososuStationDiffCallback : DiffUtil.ItemCallback<YososuStation>() {

    override fun areItemsTheSame(oldItem: YososuStation, newItem: YososuStation): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: YososuStation, newItem: YososuStation): Boolean {
        return oldItem.code == newItem.code
    }
}