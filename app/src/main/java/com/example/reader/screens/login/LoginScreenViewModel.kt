package com.example.reader.screens.login

import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


class LoginScreenViewModel : ViewModel() {
    //    val loadingState = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading


    // run away from main thread.
    fun signInWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FBR", "signInWithEmailAndPassword: ye ${task.result}")
                        // TODO: ("take them home Screen")
                    } else {
                        Log.d("FB r", "signInWithEmailAndPassword: ${task.result}")
                    }
                }
        } catch (ex: Exception) {
            Log.d("FB", "SignInWithEmailAndPassword: ${ex.message}")
        }
    }

    // run away from main thread.
    fun createUserWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {

    }
}