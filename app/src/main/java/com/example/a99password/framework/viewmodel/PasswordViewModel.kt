package com.example.a99password.framework.viewmodel

import androidx.lifecycle.*
import com.example.a99password.data.PasswordRepository
import com.example.a99password.domain.GroupPassword
import com.example.a99password.domain.Password
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class PasswordViewModel(
    private val passwordRepository: PasswordRepository
) : ViewModel() {

    fun getAllPassword(search : String) : LiveData<List<Password>> = passwordRepository.getAllPassword(search).asLiveData()

    fun insertPassword(password: Password) = viewModelScope.launch {
        passwordRepository.insertPassword(password)
    }

    fun updatePassword(password: Password) = viewModelScope.launch {
        passwordRepository.updatePassword(
            newName = password.passwordName,
            newEmail = password.passwordEmail,
            newPassword = password.passwordPassword,
            idPassword = password.id!!
        )
    }

    fun deletePassword(idPassword : Int) = viewModelScope.launch {
        passwordRepository.deletePassword(idPassword)
    }
}

class PasswordViewModelFactory(private val passwordRepository: PasswordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PasswordViewModel::class.java)){
            return PasswordViewModel(passwordRepository) as T
        }
        throw IllegalArgumentException("Viewmodel desconhecida")
    }
}