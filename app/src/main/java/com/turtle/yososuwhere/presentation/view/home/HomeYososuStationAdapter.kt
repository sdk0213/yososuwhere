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
import com.turtle.yososuwhere.databinding.ListItemHomeYososuBinding
import com.turtle.yososuwhere.domain.model.YososuStation


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
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:${tvListItemHomeGasStationTel.text}")
                    }
                    startActivity(mContext, intent, null)
                }
                tvListItemHomeGasStationNumber.text = "(${position + 1}/${itemCount})"
                tvListItemHomeGasStationAddr.text = item.addr
                tvListItemHomeGasStationName.text = item.name
                tvListItemHomeGasStationTel.text = item.tel
                tvListItemHomeGasStationHoursOfOperation.text = "영업시간 : ${item.operationTime}"
                tvListItemHomeGasStationYososuStock.text =
                    if (item.stock == 0L) "요소수 없음" else "요소수 재고 : ${item.stock}"

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