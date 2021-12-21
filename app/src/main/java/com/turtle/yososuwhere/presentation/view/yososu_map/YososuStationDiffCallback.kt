package com.turtle.yososuwhere.presentation.view.yososu_map

import androidx.recyclerview.widget.DiffUtil
import com.turtle.yososuwhere.domain.model.YososuStation

class YososuStationDiffCallback : DiffUtil.ItemCallback<YososuStation>() {

    override fun areItemsTheSame(oldItem: YososuStation, newItem: YososuStation): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: YososuStation, newItem: YososuStation): Boolean {
        return oldItem.code == newItem.code
    }
}