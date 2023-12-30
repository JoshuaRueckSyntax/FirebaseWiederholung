package com.example.firebasewiederholung.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.firebasewiederholung.FirebaseViewModel
import com.example.firebasewiederholung.R
import com.example.firebasewiederholung.databinding.FragmentUserBinding
import com.example.firebasewiederholung.model.Profile
import coil.load

class UserFragment: Fragment() {

    private lateinit var binding: FragmentUserBinding
    private val viewModel: FirebaseViewModel by activityViewModels()

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            viewModel.uploadImage(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.profileRef.addSnapshotListener { value, error ->
            if (error == null && value != null) {

                val myProfile = value.toObject(Profile::class.java)

                if (myProfile != null) {
                    binding.etUsername.setText(myProfile.username)
                    binding.ivProfilepicture.load(myProfile.profilePicture)

                    if(myProfile.username != ""){
                        binding.tvHello.text = "Hello " + myProfile.username + "!"
                    }

                    if(myProfile.profilePicture == ""){
                        binding.ivProfilepicture.load(R.drawable.placeholder)
                    }
                } else {
                    binding.etUsername.setText("")
                }
            }
        }

        viewModel.currentUser.observe(viewLifecycleOwner) {
            if (it == null) {
                findNavController().navigate(R.id.loginFragment)
            }
        }

        binding.btUpdateuser.setOnClickListener {
            val username = binding.etUsername.text.toString()

            if(username != ""){
                viewModel.updateUsername(username)
                Toast.makeText(getActivity(),"Username updated", Toast.LENGTH_SHORT).show()
            }
        }

        binding.ibEditprofile.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.ibDelprofilepic.setOnClickListener {
            viewModel.emptyPicture()
            Toast.makeText(getActivity(),"Profile picture removed", Toast.LENGTH_SHORT).show()
        }
    }
}