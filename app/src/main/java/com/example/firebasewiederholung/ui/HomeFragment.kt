package com.example.firebasewiederholung.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.firebasewiederholung.FirebaseViewModel
import com.example.firebasewiederholung.R
import com.example.firebasewiederholung.databinding.FragmentHomeBinding

class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btLogout.setOnClickListener {
            viewModel.logout()
        }

        viewModel.currentUser.observe(viewLifecycleOwner) {
            if (it == null) {
                findNavController().navigate(R.id.loginFragment)
            }
        }

    }

}