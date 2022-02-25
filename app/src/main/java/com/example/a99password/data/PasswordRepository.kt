package com.example.a99password.data

import com.example.a99password.domain.GroupPassword
import com.example.a99password.domain.Password
import com.example.a99password.framework.DAO
import kotlinx.coroutines.flow.Flow

class PasswordRepository(
    private val passwordDAO : DAO
) {

    fun getAllPassword(search : String) : Flow<List<Password>> = passwordDAO.getAllPassword(search)

    fun getAlGroupPassword(search : String) : Flow<List<GroupPassword>> = passwordDAO.getAllGroups(search)

    suspend fun updatePassword(newName : String, newEmail : String, newPassword : String, idPassword : Int) = passwordDAO.updatePassword(newName, newEmail, newPassword, idPassword)

    suspend fun updateGroupPassword(newName: String, newGroupPassword: String?, idGroupPassword: Int) = passwordDAO.updateGroupPassword(newName, newGroupPassword, idGroupPassword)

    suspend fun deletePassword(idPassword: Int) = passwordDAO.deletePassword(idPassword)

    suspend fun deleteGroupPassword(idGroupPassword: Int) = passwordDAO.deleteGroupPassword(idGroupPassword)

    suspend fun insertGroup(groupPassword: GroupPassword) {
        passwordDAO.insertGroup(groupPassword)
    }

    suspend fun insertPassword(password: Password){
        passwordDAO.insertPassword(password)
    }
}