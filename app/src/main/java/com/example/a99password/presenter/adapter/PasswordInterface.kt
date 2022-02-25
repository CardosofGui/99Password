package com.example.a99password.presenter.adapter

import com.example.a99password.domain.Password

interface PasswordInterface : ItemAction {

    fun copyToClipboard(textToCopy : String)

    fun clickPassword(password : Password, statusSelected : Boolean) : Boolean?
}