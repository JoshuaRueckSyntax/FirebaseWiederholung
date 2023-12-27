package com.example.firebasewiederholung

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseViewModel: ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private var _currentUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser

    fun register(email: String, password: String, callback: (Boolean) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {
                firebaseAuth.currentUser?.sendEmailVerification()
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

}