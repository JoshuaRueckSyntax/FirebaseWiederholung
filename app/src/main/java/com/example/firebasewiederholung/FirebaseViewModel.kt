package com.example.firebasewiederholung

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firebasewiederholung.model.Profile
import com.example.firebasewiederholung.model.TodoItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseViewModel: ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val fireStore = FirebaseFirestore.getInstance()

    val todolistRef = fireStore.collection("todo")

    private var _currentUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser

    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    lateinit var profileRef: DocumentReference

    init {
        if (firebaseAuth.currentUser != null) {
            profileRef = fireStore.collection("profiles").document(firebaseAuth.currentUser!!.uid)
        }
    }

    fun register(email: String, password: String, callback: (Boolean) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {
                firebaseAuth.currentUser?.sendEmailVerification()
                profileRef = fireStore.collection("profiles").document(firebaseAuth.currentUser!!.uid)
                profileRef.set(Profile())
                logout()
            } else {
                Log.e("FIREBASE", "${authResult.exception}")
            }
            callback(authResult.isSuccessful)
        }
    }

    fun login(email: String, password: String, callback: (Boolean) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {
                if (firebaseAuth.currentUser!!.isEmailVerified) {
                    _currentUser.value = firebaseAuth.currentUser
                    profileRef = fireStore.collection("profiles").document(firebaseAuth.currentUser!!.uid)
                } else {
                    Log.e("FIREBASE", "User not verified")
                    logout()
                    callback(false)
                }
            } else {
                Log.e("FIREBASE", "${authResult.exception}")
                callback(authResult.isSuccessful)
            }
        }
    }

    fun sendPasswordReset(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
    }

    fun logout() {
        firebaseAuth.signOut()
        _currentUser.value = firebaseAuth.currentUser
    }

    fun saveTodo(todo: TodoItem){
        todolistRef.add(todo)
    }

    fun deleteTodo(todo: TodoItem){
        todolistRef.document(todo.id).delete()
    }

    private fun setUserImage(uri: Uri) {
        profileRef.update("profilePicture", uri.toString())
    }

    fun uploadImage(uri: Uri) {
        val imageRef = storageRef.child("images/${firebaseAuth.currentUser!!.uid}/profilePic")
        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnCompleteListener {
            imageRef.downloadUrl.addOnCompleteListener {
                if (it.isSuccessful) {
                    setUserImage(it.result)
                }
            }
        }
    }

    fun emptyPicture() {
        profileRef.update("profilePicture", "")
    }

    fun updateUsername(username: String){
        profileRef.update("username", username)
    }

}