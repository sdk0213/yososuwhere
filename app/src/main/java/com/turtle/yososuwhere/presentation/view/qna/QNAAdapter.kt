package com.turtle.yososuwhere.presentation.view.qna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.turtle.yososuwhere.databinding.ListItemQnaBinding
import com.turtle.yososuwhere.domain.model.QnaData
import com.turtle.yososuwhere.presentation.utilities.ToggleAnimation

class QNAAdapter : ListAdapter<QnaData, QNAAdapter.QNAViewHolder>(QNADiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QNAViewHolder {
        return QNAViewHolder(
            ListItemQnaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: QNAViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class QNAViewHolder(
        private val binding: ListItemQnaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(qna: QnaData) {
            binding.tvQnaQuestion.text = qna.question
            binding.tvQnaAnswer.text = qna.answer
            binding.imgButtonQnaDown.setOnClickListener {
                qna.isExpanded = toggleLayout(!qna.isExpanded, it, binding.linearlayoutQnaAnswer)
            }
        }

    }

    private fun toggleLayout(isExpanded: Boolean, view: View, layoutExpand: LinearLayout): Boolean {
        // 2
        ToggleAnimation.toggleArrow(view, isExpanded)
        if (isExpanded) {
            ToggleAnimation.expand(layoutExpand)
        } else {
            ToggleAnimation.collapse(layoutExpand)
        }
        return isExpanded
    }

}

class QNADiffCallback : DiffUtil.ItemCallback<QnaData>() {

    override fun areItemsTheSame(oldItem: QnaData, newItem: QnaData): Boolean {
        return oldItem.answer == newItem.answer
    }

    override fun areContentsTheSame(oldItem: QnaData, newItem: QnaData): Boolean {
        return oldItem.answer == newItem.answer
    }
}