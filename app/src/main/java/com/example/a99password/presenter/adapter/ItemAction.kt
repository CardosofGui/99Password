package com.example.a99password.presenter.adapter

import com.example.a99password.domain.GroupPassword
import com.example.a99password.domain.Password

interface ItemAction {

    fun editItem(password : Password?, groupPassword: GroupPassword?)

    fun deleteItem(id : Int)
}