package com.example.a99password.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "password_table")
data class Password(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id_password") val id : Int?,
    @ColumnInfo(name = "password_name") val passwordName : String,
    @ColumnInfo(name = "password_email") val passwordEmail : String,
    @ColumnInfo(name = "password_password") val passwordPassword : String
)
