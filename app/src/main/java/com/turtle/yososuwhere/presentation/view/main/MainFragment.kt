package com.turtle.yososuwhere.presentation.view.main

import com.turtle.yososuwhere.R
import com.turtle.yososuwhere.databinding.FragmentMainBinding
import com.turtle.yososuwhere.presentation.view.base.BaseFragment

class MainFragment :
    BaseFragment<MainViewModel, FragmentMainBinding>(R.layout.fragment_main) {

    override fun init() {
        binding.viewModel = viewModel
    }

}
