package com.example.firebasewiederholung.model

import com.google.firebase.firestore.DocumentId

data class TodoItem(
    @DocumentId
    val id: String = "",
    val text: String = "",
    var checked: Boolean = false,
    val timestamp: Long = 0
)