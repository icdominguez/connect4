package com.icdominguez.connect4.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.icdominguez.connect4.R
import com.icdominguez.connect4.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.buttonPlay.setOnClickListener {
            requireView().findNavController().navigate(R.id.action_homeFragment_to_gameFragment)
        }

        binding.buttonInfo.setOnClickListener {
            requireView().findNavController().navigate(R.id.action_homeFragment_to_infoFragment)
        }
    }
}