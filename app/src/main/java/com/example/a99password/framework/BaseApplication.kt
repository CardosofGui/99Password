package com.example.a99password.framework

import android.app.Application
import com.example.a99password.data.PasswordDatabase
import com.example.a99password.data.PasswordRepository

class BaseApplication : Application() {

    val database by lazy { PasswordDatabase.getDatabase(this) }
    val repository by lazy { PasswordRepository(database.passwordDAO()) }

}