package com.example.a99password.presenter.adapter

import com.example.a99password.domain.GroupPassword
import com.example.a99password.domain.Password

interface GroupInterface : ItemAction {

    fun expandGroupClicK(listPassword : List<Password>?)

    fun editGroupClick(groupPassword: GroupPassword)

}