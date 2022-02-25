package com.example.a99password.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.a99password.domain.GroupPassword
import com.example.a99password.domain.Password
import com.example.a99password.framework.DAO

@Database(entities = arrayOf(Password::class, GroupPassword::class), version = 1, exportSchema = false)

public abstract class PasswordDatabase : RoomDatabase() {

    abstract fun passwordDAO() : DAO

    companion object {
        @Volatile
        private var INSTANCE : PasswordDatabase? = null

        fun getDatabase(
            context: Context
        ) : PasswordDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PasswordDatabase::class.java,
                    "password_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}