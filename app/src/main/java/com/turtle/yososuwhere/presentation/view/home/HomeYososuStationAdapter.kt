package com.turtle.yososuwhere.presentation.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.turtle.yososuwhere.databinding.ListItemHomeYososuBinding
import com.turtle.yososuwhere.domain.model.YososuStation

class HomeYososuStationAdapter constructor(
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
        holder.bind(getItem(position))
    }

    inner class HomeYososuStationViewHolder(
        private val binding: ListItemHomeYososuBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: YososuStation) {
            binding.apply {
                tvListItemHomeGasStationAddr.text = item.주소
                tvListItemHomeGasStationName.text = item.명칭
                tvListItemHomeGasStationTel.text = item.전화번호
                tvListItemHomeGasStationHoursOfOperation.text = item.영업시간
                tvListItemHomeGasStationYososuStock.text = item.재고량

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