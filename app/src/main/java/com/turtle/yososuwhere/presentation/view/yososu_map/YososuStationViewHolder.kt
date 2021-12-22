package com.turtle.yososuwhere.presentation.view.yososu_map

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.turtle.yososuwhere.R
import com.turtle.yososuwhere.databinding.ListItemHomeYososuBinding
import com.turtle.yososuwhere.domain.model.YososuStation
import com.turtle.yososuwhere.presentation.utilities.extensions.convertToDateDetail
import com.turtle.yososuwhere.presentation.utilities.extensions.getCountHourAndtime
import java.util.*

class YososuStationViewHolder(
    private val binding: ListItemHomeYososuBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(mContext: Context, item: YososuStation) {
        binding.apply {
            btnListItemHomeGasStationTel.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${btnListItemHomeGasStationTel.text}")
                }
                ContextCompat.startActivity(mContext, intent, null)
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
            tvListItemHomeGasStationHoursOfOperation.text = "영업시간 : ${item.operationTime}"
            tvListItemHomeGasStationYososuStock.text =
                if (hasYososu) "요소수 없음" else "재고 : ${item.stock}"
            tvListItemHomeGasStationYososuCost.text = if(item.cost == "undefined") "가격 : 미정" else "가격 : ${item.cost}원"
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
                val clipboard: ClipboardManager =
                    mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("", item.addr)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(mContext, "주유소 주소를 복사하였습니다.", Toast.LENGTH_SHORT).show()
            }

            executePendingBindings()
        }
    }

}