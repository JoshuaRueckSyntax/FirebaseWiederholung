package com.example.firebasewiederholung.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.firebasewiederholung.FirebaseViewModel
import com.example.firebasewiederholung.R
import com.example.firebasewiederholung.databinding.FragmentLoginBinding

class LoginFragment: Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ibHome.setOnClickListener {
            findNavController().navigate(R.id.welcomeFragment)
        }

        binding.btLogin.setOnClickListener {
            val email = binding.etEmailLog.text.toString()
            val password = binding.etPassLog.text.toString()

            if (email != "" && password != "") {
                viewModel.login(email, password){success ->
                    if(!success){
                        binding.etEmailLog.text.clear()
                        binding.etPassLog.text.clear()
                        hidekeyboard(view)
                        Toast.makeText(getActivity(),"Not verified or wrong e-mail/password.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btResetpass.setOnClickListener {
            val email = binding.etEmailLog.text.toString()
            if(email != "") {
                viewModel.sendPasswordReset(email)
                Toast.makeText(getActivity(),"Password reset e-mail sent.", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.currentUser.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(R.id.homeFragment)
            }
        }
    }

    fun hidekeyboard(view: View){
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }
}