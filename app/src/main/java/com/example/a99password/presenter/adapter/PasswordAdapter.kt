package com.example.a99password.presenter.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.example.a99password.R
import com.example.a99password.databinding.CardviewPasswordBinding
import com.example.a99password.domain.Password

class PasswordAdapter(
    private var listPassword : List<Password>,
    private val passwordInterface: PasswordInterface,
    private val passwordVerify : Boolean,
    private var editItemEnable : Boolean
) : RecyclerView.Adapter<PasswordViewHolder>() {
    private var selectedList = arrayListOf<Password>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordViewHolder {
        return PasswordViewHolder(CardviewPasswordBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        val binding = holder.binding
        val password = listPassword[position]

        val passwordLength = password.passwordPassword.length
        var statusVisibility = true

        binding.tvNamePassword.setText(password.passwordName)
        binding.tvPasswordPassword.setText("Senha: " + "*".repeat(passwordLength))
        binding.tvEmailUser.setText("E-mail: " + password.passwordEmail)

        if(editItemEnable) {
            binding.root.setOnLongClickListener { view ->

                val popupMenu = PopupMenu(view.context, view)
                popupMenu.menuInflater.inflate(R.menu.menu_item, popupMenu.menu)

                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.item_delete -> {
                            passwordInterface.deleteItem(password.id!!)
                        }
                        R.id.item_edit -> {
                            passwordInterface.editItem(password, null)
                        }
                    }

                    return@setOnMenuItemClickListener false
                }

                popupMenu.show()

                return@setOnLongClickListener false
            }
        }

        binding.ibVisible.setOnClickListener {
            if(statusVisibility) {
                statusVisibility = false
                binding.ibVisible.setImageResource(R.drawable.ic_baseline_visibility_off_24)
                binding.tvPasswordPassword.setText("Senha: " + password.passwordPassword)
            } else {
                statusVisibility = true
                binding.ibVisible.setImageResource(R.drawable.ic_baseline_visibility_24)
                binding.tvPasswordPassword.setText("Senha: " + "*".repeat(passwordLength))
            }
        }

        binding.ibCopy.setOnClickListener {
            passwordInterface.copyToClipboard(password.passwordPassword)
        }

        binding.root.setOnClickListener {
            if(passwordInterface.clickPassword(password, selectedList.contains(password)) == true){
                selectedList.add(password)
                notifyItemChanged(position)
            } else if(passwordInterface.clickPassword(password, selectedList.contains(password)) == false) {
                selectedList.remove(password)
                notifyItemChanged(position)
            }
        }

        if(selectedList.contains(password)) binding.ctlPassword.setBackgroundResource(R.drawable.background_borded)
        else binding.ctlPassword.setBackgroundResource(R.drawable.background_borded_white)


        if(passwordVerify){
            binding.ibVerifyPassword.visibility = View.VISIBLE

            val verify = isValidPassword(password.passwordPassword)
            var stringRecomendation = ""
            var count = 1

            verify.forEach {
                stringRecomendation += "$count. $it;\n"
                count += 1
            }

            when(verify.size) {
                2 -> {
                    binding.ibVerifyPassword.setImageResource(R.drawable.ic_alert_icon)
                }
                3,4 -> {
                    binding.ibVerifyPassword.setImageResource(R.drawable.ic_low_icon)
                }
                1 -> {
                    binding.ibVerifyPassword.setImageResource(R.drawable.ic_strong_icon)
                }
                0 -> {
                    binding.ibVerifyPassword.setImageResource(R.drawable.ic_strong_icon)
                    stringRecomendation = "Senha forte, sem recomendações!"
                }
            }

            binding.tvListRecomendation.setText(stringRecomendation)

            var showRecomendation = false

            binding.ibVerifyPassword.setOnClickListener {
                if(!showRecomendation) {
                    binding.tvRecomendation.visibility = View.VISIBLE
                    binding.tvListRecomendation.visibility = View.VISIBLE
                    showRecomendation = true
                } else {
                    binding.tvRecomendation.visibility = View.GONE
                    binding.tvListRecomendation.visibility = View.GONE
                    showRecomendation = false
                }
            }
        }
    }

    fun setSelectedList(newSelectedList : List<Password>){
        selectedList = newSelectedList as ArrayList<Password>
        notifyDataSetChanged()
    }

    fun isValidPassword(password: String): ArrayList<String> {
        val passwordStrength = arrayListOf<String>()

        if (!password.isLongEnough()) passwordStrength.add("Que seja maior ou igual a oito caracteres")
        if (!password.isMixedCase()) passwordStrength.add("Tenha pelo o menos uma letra maiúsculo e uma minúsculo")
        if (!password.hasEnoughDigits()) passwordStrength.add("Tenha pelo o menos um número")
        if (!password.hasSpecialChar()) passwordStrength.add("Tenha pelo o menos um caractere especial")

        return passwordStrength
    }

    fun submitList(newList : List<Password>) {
        listPassword = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = listPassword.size
}

class PasswordViewHolder(val binding : CardviewPasswordBinding) : RecyclerView.ViewHolder(binding.root)

fun String.isLongEnough() = length >= 8
fun String.hasEnoughDigits() = count(Char::isDigit) > 0
fun String.isMixedCase() = any(Char::isLowerCase) && any(Char::isUpperCase)
fun String.hasSpecialChar() = any { it in "!,+^@_-" }
