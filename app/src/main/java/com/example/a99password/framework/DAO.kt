package com.example.a99password.framework

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.a99password.domain.GroupPassword
import com.example.a99password.domain.Password
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {

    @Query("SELECT * FROM password_table WHERE password_name LIKE '%' || :search || '%' OR password_email LIKE'%' || :search || '%'")
    fun getAllPassword(search : String) : Flow<List<Password>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPassword(password: Password)

    @Query("SELECT * FROM table_group_password WHERE group_name LIKE '%' || :search || '%'")
    fun getAllGroups(search : String) : Flow<List<GroupPassword>>

    @Query("UPDATE password_table SET password_name = :newName, password_email = :newEmail, password_password = :newPassword WHERE id_password = :idPassword")
    suspend fun updatePassword(newName : String, newEmail : String, newPassword: String, idPassword: Int)

    @Query("UPDATE table_group_password SET group_name = :newName, group_passwords = :newGroupPassword WHERE id_group = :idGroup")
    suspend fun updateGroupPassword(newName : String, newGroupPassword : String?, idGroup: Int)

    @Query("DELETE FROM table_group_password WHERE id_group = :idGroup")
    suspend fun deleteGroupPassword(idGroup: Int)

    @Query("DELETE FROM password_table WHERE id_password = :idPassword")
    suspend fun deletePassword(idPassword: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGroup(groupPassword: GroupPassword)
}