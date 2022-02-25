package com.example.a99password.presenter.adapter

import android.app.Activity
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.a99password.*
import com.example.a99password.databinding.CardviewOptionsBinding
import com.example.a99password.presenter.AllPasswordsFragment
import com.example.a99password.presenter.GroupPasswordsFragment
import com.example.a99password.presenter.ProfileFragment
import com.example.a99password.presenter.VerifyPasswordFragment

class OptionsAdapter(
    private val optionsInterface: OptionsInterface
) : RecyclerView.Adapter<OptionsViewHolder>() {

    private val optionAllPassword = Option(AllPasswordsFragment(), R.drawable.all_passwords, "Todas as senhas")
    private val optionGroupPassword = Option(GroupPasswordsFragment(), R.drawable.group_passwords, "Grupos de senhas")
    private val optionVerifyPassword = Option(VerifyPasswordFragment(), R.drawable.strong_passwords, "Verificar senhas")
    private val optionConfig = Option(ProfileFragment(), R.drawable.ic_settings_svgrepo_com, "Configuração")

    private val optionList = arrayListOf<Option>(optionAllPassword, optionGroupPassword, optionVerifyPassword, optionConfig)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
        return OptionsViewHolder(CardviewOptionsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
        val binding = holder.binding
        val option = optionList[position]

        binding.ivItemIcon.setImageResource(option.drawableID)
        binding.tvTitleItem.setText(option.title)

        binding.root.setOnClickListener {
            optionsInterface.initOption(option.fragment, option.title)
        }
    }

    override fun getItemCount(): Int = optionList.size
}

class OptionsViewHolder(val binding : CardviewOptionsBinding) : RecyclerView.ViewHolder(binding.root)

data class Option(
    val fragment : Fragment,
    val drawableID : Int,
    val title : String
)