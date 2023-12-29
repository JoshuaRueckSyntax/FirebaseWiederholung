package com.example.firebasewiederholung.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldValue
import java.util.Date

data class TodoItem(
    @DocumentId
    val id: String = "",
    val text: String = "",
    var checked: Boolean = false,
    val timestamp: Long = 0
)