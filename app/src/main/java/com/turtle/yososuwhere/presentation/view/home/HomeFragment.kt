package com.turtle.yososuwhere.presentation.view.home

import com.turtle.yososuwhere.R
import com.turtle.yososuwhere.databinding.FragmentHomeBinding
import com.turtle.yososuwhere.presentation.view.base.BaseFragment

class HomeFragment :
    BaseFragment<HomeViewModel, FragmentHomeBinding>(R.layout.fragment_home) {

    override fun init() {
        view()
        viewModel()
        listener()
        observer()

    }

    private fun view() {

    }

    private fun viewModel() {
        binding.viewModel = viewModel
    }

    private fun listener() {

    }

    private fun observer() {


    }

}
