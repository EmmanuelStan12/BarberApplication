package com.ammar.vendorapp.authentication.ui.sign_up

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ammar.vendorapp.authentication.common.utils.Result
import com.ammar.vendorapp.authentication.data.models.UserRegister
import com.ammar.vendorapp.authentication.domain.entities.User
import com.ammar.vendorapp.authentication.domain.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    private val _state = MutableStateFlow(SignupState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<SignupUiEvents>()
    val events = _events.asSharedFlow()

    val execute: (SignupEvents) -> Unit = { event ->
        viewModelScope.launch {
            when(event) {
                is SignupEvents.Register -> {
                    val user = UserRegister(
                        email = state.value.email.value,
                        password = state.value.password.value,
                        username = state.value.username.value,
                        firstName = state.value.firstname.value,
                        lastName = state.value.lastname.value,
                    )
                    repository.register(user).collectLatest { result ->
                        when(result) {
                            is Result.Success -> {
                                result.data
                            }
                            is Result.Error -> {

                            }
                            is Result.Loading -> {

                            }
                        }
                    }
                    if(state.value.isValid()) {

                    } else {
                        _events.emit(SignupUiEvents.InvalidInputParameters)
                    }
                }
                is SignupEvents.ChangeEmail -> {
                    _state.changeEmailState(event.email, event.error)
                }
                is SignupEvents.ChangeFirstname -> {
                    _state.changeFirstName(event.value, event.error)
                }
                is SignupEvents.ChangeLastname -> {
                    _state.changeLastName(event.value, event.error)
                }
                is SignupEvents.ChangePassword -> {
                    _state.changePasswordState(event.password, event.error)
                }
                is SignupEvents.ChangeUsername -> {
                    _state.changeUsername(event.value, event.error)
                }
            }
        }

    }

    private fun print(data: String?, tag: String = "TAG") {
        Log.d(tag, "print: ${data.toString()}")
    }

}

sealed class SignupUiEvents {
    object InvalidInputParameters: SignupUiEvents()
}