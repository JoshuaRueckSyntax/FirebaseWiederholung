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
import com.example.firebasewiederholung.adapter.TodoAdapter
import com.example.firebasewiederholung.databinding.FragmentHomeBinding
import com.example.firebasewiederholung.model.TodoItem
import com.google.firebase.firestore.toObject

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

        viewModel.todolistRef.addSnapshotListener { value, error ->
            if (error == null && value != null) {
                val todoList = value.map { it.toObject(TodoItem::class.java) }
                val todoListUnchecked = todoList.filter { !it.checked }
                val todoListChecked = todoList.filter { it.checked }
                binding.rvTodo.adapter = TodoAdapter(todoListUnchecked, viewModel)
                binding.rvTodochecked.adapter = TodoAdapter(todoListChecked, viewModel)
            }
        }

        binding.ibSavetodo.setOnClickListener {
            val todoText = binding.etNewtodo.text.toString()
            if(todoText != ""){
                viewModel.saveTodo(TodoItem(text = todoText))
                binding.etNewtodo.text.clear()
            }
        }

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