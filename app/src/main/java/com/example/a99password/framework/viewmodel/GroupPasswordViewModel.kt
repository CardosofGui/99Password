package com.example.a99password.framework.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.example.a99password.data.PasswordRepository
import com.example.a99password.domain.GroupPassword
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class GroupPasswordViewModel(private val passwordRepository: PasswordRepository) : ViewModel() {

    fun getListGroupPassword(search : String) : LiveData<List<GroupPassword>> {
        return passwordRepository.getAlGroupPassword(search).asLiveData()
    }

    fun insertGroupPassword(groupPassword: GroupPassword) = viewModelScope.launch {
        passwordRepository.insertGroup(groupPassword)
    }

    fun updateGroupPassword(groupPassword: GroupPassword) = viewModelScope.launch {
        passwordRepository.updateGroupPassword(
            newName = groupPassword.name,
            newGroupPassword = groupPassword.passwords,
            idGroupPassword = groupPassword.id!!
        )
    }

    fun deleteGroupPassword(idGroupPassword : Int) = viewModelScope.launch {
        passwordRepository.deleteGroupPassword(idGroupPassword)
    }
}


class GroupPasswordViewModelFactory(private val passwordRepository: PasswordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GroupPasswordViewModel::class.java)){
            return GroupPasswordViewModel(passwordRepository) as T
        }
        throw IllegalArgumentException("Viewmodel desconhecida")
    }
}
