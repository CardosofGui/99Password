package com.example.a99password.presenter

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import com.example.a99password.R
import com.example.a99password.databinding.ActivityRegisterGroupBinding
import com.example.a99password.domain.GroupPassword
import com.example.a99password.domain.Password
import com.example.a99password.framework.BaseApplication
import com.example.a99password.framework.viewmodel.PasswordViewModel
import com.example.a99password.framework.viewmodel.PasswordViewModelFactory
import com.example.a99password.presenter.GroupPasswordsFragment.Companion.GROUP_ID
import com.example.a99password.presenter.GroupPasswordsFragment.Companion.GROUP_NAME
import com.example.a99password.presenter.GroupPasswordsFragment.Companion.GROUP_PASSWORD_LIST
import com.example.a99password.presenter.adapter.GroupAdapter
import com.example.a99password.presenter.adapter.PasswordAdapter
import com.example.a99password.presenter.adapter.PasswordInterface
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import java.security.acl.Group

class RegisterGroup : AppCompatActivity(), PasswordInterface {


    private val passwordViewModel : PasswordViewModel by viewModels {
        PasswordViewModelFactory((application as BaseApplication).repository)
    }

    private val binding by lazy { ActivityRegisterGroupBinding.inflate(layoutInflater) }
    private var listPasswordSelected : ArrayList<Password> = arrayListOf()
    private lateinit var passwordAdapter: PasswordAdapter

    private var passwordID = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRecycler()
        setData()
        initClicks()
        initSearch()
    }

    private fun setData() {
        val requestCode = intent.getIntExtra("requestCode", -1)

        if(requestCode == 2002){
            val name = intent.getStringExtra(GROUP_NAME)
            val passwordList = intent.getStringExtra(GROUP_PASSWORD_LIST)
            passwordID = intent.getIntExtra(GROUP_ID, -1)

            val groupPassword = GroupPassword(passwordID, name!!, passwordList)

            passwordAdapter.setSelectedList(groupPassword.getListPasswords() ?: arrayListOf())
            listPasswordSelected = groupPassword.getListPasswords() as ArrayList<Password>

            binding.tieNamePassword.setText(name)
            binding.btnRegister.setText("Atualizar")
        }
    }

    private fun initSearch() {
        binding.tieSearchPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val search = binding.tieSearchPassword.text.toString()

                passwordViewModel.getAllPassword(search).observe(this@RegisterGroup, { listPassword ->
                    passwordAdapter.submitList(listPassword)
                })
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun initRecycler() {
        passwordAdapter = PasswordAdapter(arrayListOf(), this, passwordVerify = false, editItemEnable = false)
        binding.rcvPasswords.adapter = passwordAdapter

        passwordViewModel.getAllPassword("").observe(this, { listPassword ->
            passwordAdapter.submitList(listPassword)
        })
    }

    private fun initClicks() {
        binding.btnRegister.setOnClickListener {
            if(!binding.tieNamePassword.text.isNullOrEmpty()){
                registerGroup()
            }
        }
    }

    private fun registerGroup() {
        val name = binding.tieNamePassword.text.toString()
        val listPasswords = if(listPasswordSelected.size == 0) null else Gson().toJson(listPasswordSelected)

        Intent().let {
            it.putExtra(GROUP_ID, passwordID)
            it.putExtra(GROUP_NAME, name)
            it.putExtra(GROUP_PASSWORD_LIST, listPasswords)

            setResult(Activity.RESULT_OK, it)
        }

        finish()
    }

    override fun copyToClipboard(textToCopy: String) {
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(textToCopy, textToCopy)
        clipboardManager.setPrimaryClip(clipData)

        FancyToast.makeText(this,"Senha copiada!", FancyToast.LENGTH_LONG, FancyToast.INFO,false).show()
    }

    override fun clickPassword(password: Password, statusSelected: Boolean): Boolean {
        return if(!statusSelected){
            listPasswordSelected.add(password)
            true
        } else {
            listPasswordSelected.remove(password)
            false
        }
    }

    override fun editItem(password: Password?, groupPassword: GroupPassword?) {}

    override fun deleteItem(id: Int) {}
}