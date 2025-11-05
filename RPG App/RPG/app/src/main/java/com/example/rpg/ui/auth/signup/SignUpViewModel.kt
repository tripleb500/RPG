package com.example.rpg.ui.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpg.data.model.User
import com.example.rpg.data.repository.AuthRepository
import com.example.rpg.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    //  Holds string; current user error if necessary, when signing up.
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Signup function called from SignUpScreen. Takes user's email, password, username, and family role (parent or child).
    fun signUp(
        firstname: String,
        lastname: String,
        email: String,
        password: String,
        username: String,
        role: String,
        onSuccess: (Boolean, String?) -> Unit
    ) {
        if (firstname.isBlank()) {
            _errorMessage.value = "First Name empty, Please enter your first name."
            return
        }
        if (lastname.isBlank()) {
            _errorMessage.value = "Last Name empty, Please enter your last name."
            return
        }
        if (email.isBlank()) {
            _errorMessage.value = "Email empty, Please enter an email."
            return
        }
        if (password.isBlank()) {
            _errorMessage.value = "Password empty, Please enter a password."
            return
        }

        viewModelScope.launch {
            try {
                //  Enforces unique username. Users cannot have the same username now when signing up
                val existingUser = userRepository.getUserByUsername(username)
                if (existingUser != null) {
                    _errorMessage.value = "Username already taken."
                    onSuccess(false, null)
                    return@launch
                }
                authRepository.signUp(
                    email,
                    password
                )  // Calls AuthRepository to create user in Firebase Auth.
                val userId = authRepository.currentUser?.uid
                    ?: return@launch  // After signup, get current Firebase user's UID (unique ID).

                val user = User(  // Construct a User data class object
                    id = userId,
                    firstname = firstname,
                    lastname = lastname,
                    username = username,
                    email = email,
                    familyRole = role
                )
                userRepository.createProfile(user)  // Calls UserRepository to save the profile to Firestore database. Ensures that we have a user profile instead of just user authentication.
                onSuccess(
                    true,
                    role
                )  // Tells UI that signup was successful. Role is passed so UI can navigate to Parent or Child screen based upon selection.

            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "An error occurred"
                onSuccess(false, null)
            }
        }
    }
}