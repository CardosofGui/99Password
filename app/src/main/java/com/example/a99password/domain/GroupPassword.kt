package com.example.a99password.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

@Entity(tableName = "table_group_password")
data class GroupPassword (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id_group") val id : Int?,
    @ColumnInfo(name = "group_name") val name : String,
    @ColumnInfo(name = "group_passwords") val passwords : String?
) : Serializable {

    fun getListPasswords(): List<Password>? {
        return if(passwords == null) null
        else Gson().fromJson(passwords, object :
            TypeToken<List<Password>>() {}.type)
    }
}