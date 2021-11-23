package com.turtle.yososuwhere.presentation.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.turtle.yososuwhere.databinding.ListItemHomeYososuBinding
import com.turtle.yososuwhere.domain.model.YososuStation
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity


class HomeYososuStationAdapter constructor(
    private val mContext: Context
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
                tvListItemHomeGasStationTel.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:${tvListItemHomeGasStationTel.text}") }
                    startActivity(mContext, intent, null)
                }
                tvListItemHomeGasStationNumber.text = "(${position+1}/${itemCount})"
                tvListItemHomeGasStationAddr.text = item.주소
                tvListItemHomeGasStationName.text = item.명칭
                tvListItemHomeGasStationTel.text = item.전화번호
                tvListItemHomeGasStationHoursOfOperation.text = "영업시간 : ${item.영업시간}"
                tvListItemHomeGasStationYososuStock.text = if(item.재고량 == "0") "요소수 없음" else "요소수 재고 : ${item.재고량}"

                executePendingBindings()
            }
        }

    }

}

class HomeYososuStationDiffCallback : DiffUtil.ItemCallback<YososuStation>() {

    override fun areItemsTheSame(oldItem: YososuStation, newItem: YososuStation): Boolean {
        return oldItem.코드 == newItem.코드
    }

    override fun areContentsTheSame(oldItem: YososuStation, newItem: YososuStation): Boolean {
        return oldItem.코드 == newItem.코드
    }
}