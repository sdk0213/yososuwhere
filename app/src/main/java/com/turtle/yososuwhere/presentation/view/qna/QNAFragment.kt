package com.turtle.yososuwhere.presentation.view.qna

import androidx.navigation.fragment.findNavController
import com.turtle.yososuwhere.R
import com.turtle.yososuwhere.databinding.FragmentQnaBinding
import com.turtle.yososuwhere.domain.model.QnaData
import com.turtle.yososuwhere.presentation.view.base.BaseFragment


class QNAFragment : BaseFragment<QNAViewModel, FragmentQnaBinding>(R.layout.fragment_qna) {

    private lateinit var qnaAdapter: QNAAdapter

    override fun init() {
        view()
        initAdapter()
        viewModel()
        listener()
        observer()
    }


    private fun view() {

        binding.topAppBar.apply {
            setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24)
            setNavigationIconTint(0xFFFFFFFF.toInt())
            setOnClickListener {
                findNavController().navigateUp()
            }
        }

    }

    private fun initAdapter() {
        qnaAdapter = QNAAdapter()
        binding.recyclerviewQna.adapter = qnaAdapter
        val mutableQnaDataList = mutableListOf<QnaData>()
        val question = resources.getStringArray(R.array.question)
        val answer = resources.getStringArray(R.array.answer)

        for (number in question.indices) {
            mutableQnaDataList.add(
                QnaData(
                    question = question[number],
                    answer = answer[number]
                )
            )
        }
        qnaAdapter.submitList(mutableQnaDataList)
    }

    private fun viewModel() {
        binding.viewModel = viewModel
    }

    private fun listener() {
    }

    private fun observer() {
    }

}