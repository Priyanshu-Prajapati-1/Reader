package com.example.reader.screens.login

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reader.model.MUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


class LoginScreenViewModel : ViewModel() {
    //    val loadingState = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading


    val onWrongEmailPassword = mutableStateOf(false)

    // run away from main thread.
    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit) =
        viewModelScope.launch {
            onWrongEmailPassword.value = false
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            Log.d("FB", "signInWithEmailAndPassword: ye ${task.result}")
                            // TODO: ("take them home Screen")
                            home()
                        } else {
                            onWrongEmailPassword.value = true
                            Log.w("onError", "signInWithEmail:failure", task.exception)
                        }
                    }
                    .addOnFailureListener {
                        onWrongEmailPassword.value = true
                        Log.d("Failure", "on Failure : $it")
                    }

            } catch (ex: Exception) {
                Log.d("FB", "SignInWithEmailAndPassword: ${ex.message}")
            }
        }

    // run away from main thread.
    fun createUserWithEmailAndPassword(email: String, password: String, home: () -> Unit) =
        viewModelScope.launch {
            onWrongEmailPassword.value = false

            if (_loading.value == false) {
                _loading.value = true
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // me@gmail.com
                            val displayName = task.result.user?.email?.split("@")?.get(0)
                            createUser(displayName)
                            home()
                        } else {
                            Log.d("FB r", "createWithEmailAndPassword: ${task.result}")
                        }
                        _loading.value = false
                    }
                    .addOnFailureListener {
                        onWrongEmailPassword.value = true
                        Log.d("Failure", "on Failure : $it")
                    }
            }
        }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        val user = MUser(
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            quote = "life is grate",
            profession = "Android developer",
            id = null,
        ).toMap()

        FirebaseFirestore.getInstance().collection("users")
            .add(user)
    }
}