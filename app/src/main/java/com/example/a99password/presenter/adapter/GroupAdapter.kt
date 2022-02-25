package com.example.a99password.presenter.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.example.a99password.R
import com.example.a99password.data.PasswordRepository
import com.example.a99password.databinding.CardviewGroupPasswordBinding
import com.example.a99password.domain.GroupPassword
import com.example.a99password.domain.Password
import com.shashank.sony.fancytoastlib.FancyToast

class GroupAdapter(
    private var listGroupPassword : List<GroupPassword>,
    private var groupInterface: GroupInterface
) : RecyclerView.Adapter<GroupPasswordViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupPasswordViewHolder {
        return GroupPasswordViewHolder(CardviewGroupPasswordBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: GroupPasswordViewHolder, position: Int) {
        val binding = holder.binding
        val groupPassword = listGroupPassword[position]
        val listPassword = groupPassword.getListPasswords()

        binding.tvNameGroup.setText(groupPassword.name)

        binding.ibShowPasswords.setOnClickListener {
            groupInterface.expandGroupClicK(listPassword)
        }

        binding.root.setOnLongClickListener { view ->
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.menuInflater.inflate(R.menu.menu_item, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.item_delete -> {
                        groupInterface.deleteItem(groupPassword.id!!)
                    }
                    R.id.item_edit -> {
                        groupInterface.editItem(null, groupPassword)
                    }
                }

                return@setOnMenuItemClickListener false
            }

            popupMenu.show()

            return@setOnLongClickListener false
        }

        binding.ibEditGroup.setOnClickListener {
            groupInterface.editGroupClick(groupPassword)
        }
    }

    override fun getItemCount(): Int = listGroupPassword.size

    fun submitList(newList : List<GroupPassword>){
        listGroupPassword = newList
        notifyDataSetChanged()
    }
}

class GroupPasswordViewHolder(val binding : CardviewGroupPasswordBinding) : RecyclerView.ViewHolder(binding.root)