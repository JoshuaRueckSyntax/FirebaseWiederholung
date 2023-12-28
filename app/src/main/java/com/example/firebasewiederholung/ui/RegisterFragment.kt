package com.example.firebasewiederholung.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.firebasewiederholung.FirebaseViewModel
import com.example.firebasewiederholung.R
import com.example.firebasewiederholung.databinding.FragmentRegisterBinding

class RegisterFragment: Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSuccess.isVisible = false

        binding.btRegister.setOnClickListener {
            val email = binding.etEmailReg.text.toString()
            val password = binding.etPassReg.text.toString()

            if (email != "" && password != "") {
                viewModel.register(email, password){success ->
                    if(!success){
                        binding.etEmailReg.text.clear()
                        binding.etPassReg.text.clear()
                        hidekeyboard(view)
                        Toast.makeText(getActivity(),"Account creation failed.", Toast.LENGTH_SHORT).show()
                    } else {
                        binding.tvSuccess.isVisible = true
                        hidekeyboard(view)
                    }
                }
            }


        }

        viewModel.currentUser.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(R.id.homeFragment)
            }
        }

        binding.ibHome.setOnClickListener {
            findNavController().navigate(R.id.welcomeFragment)
        }

    }

    fun hidekeyboard(view: View){
        val inputMethodManager = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }

}