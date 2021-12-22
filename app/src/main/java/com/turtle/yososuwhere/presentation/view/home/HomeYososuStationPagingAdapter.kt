package com.turtle.yososuwhere.presentation.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.turtle.yososuwhere.databinding.ListItemHomeYososuBinding
import com.turtle.yososuwhere.domain.model.YososuStation
import com.turtle.yososuwhere.presentation.view.yososu_map.YososuStationDiffCallback
import com.turtle.yososuwhere.presentation.view.yososu_map.YososuStationViewHolder

class HomeYososuStationPagingAdapter constructor(
    private val mContext: Context
) : PagingDataAdapter<YososuStation, YososuStationViewHolder>(
    YososuStationDiffCallback()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): YososuStationViewHolder {
        return YososuStationViewHolder(
            ListItemHomeYososuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: YososuStationViewHolder,
        position: Int
    ) {
        getItem(position)?.let { yososuStation ->
            holder.bind(
                mContext = mContext,
                item = yososuStation
            )
        }
    }

}