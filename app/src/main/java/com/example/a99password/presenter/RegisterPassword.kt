package com.example.a99password.presenter

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a99password.R
import com.example.a99password.databinding.ActivityRegisterPasswordBinding
import com.example.a99password.presenter.AllPasswordsFragment.Companion.EMAIL_PASSWORD
import com.example.a99password.presenter.AllPasswordsFragment.Companion.NAME_PASSWORD
import com.example.a99password.presenter.AllPasswordsFragment.Companion.PASSWORD_ID
import com.example.a99password.presenter.AllPasswordsFragment.Companion.PASSWORD_PASSWORD
import com.shashank.sony.fancytoastlib.FancyToast

class RegisterPassword : AppCompatActivity() {

    private val binding : ActivityRegisterPasswordBinding by lazy { ActivityRegisterPasswordBinding.inflate(layoutInflater) }
    private var passwordID = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initSetData()
        initClicks()
    }

    private fun initSetData() {
        val requestCode = intent.getIntExtra("requestCode", -1)

        if(requestCode == 1002) {
            val passwordName = intent.getStringExtra(NAME_PASSWORD)
            val passwordEmail = intent.getStringExtra(EMAIL_PASSWORD)
            val passwordPassword = intent.getStringExtra(PASSWORD_PASSWORD)
            passwordID = intent.getIntExtra(PASSWORD_ID, -1)

            binding.tieNamePassword.setText(passwordName)
            binding.tieEmailPassword.setText(passwordEmail)
            binding.tiePasswordPassword.setText(passwordPassword)
            binding.labelRegister.setText("Editando senha")
            binding.btnRegister.setText("Editar")
        }
    }

    private fun initClicks() {
        binding.btnRegister.setOnClickListener {
            registerPassword()
        }
    }


    private fun registerPassword(){
        val name = binding.tieNamePassword.text
        val email = binding.tieEmailPassword.text
        val password = binding.tiePasswordPassword.text

        if(name.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty()){
            FancyToast.makeText(this,"Preencha todos os campos!", FancyToast.LENGTH_LONG, FancyToast.INFO,false).show()
        }else {
            Intent().let {
                if(passwordID != -1) it.putExtra(PASSWORD_ID, passwordID)
                it.putExtra(NAME_PASSWORD, name.toString())
                it.putExtra(EMAIL_PASSWORD, email.toString())
                it.putExtra(PASSWORD_PASSWORD, password.toString())

                setResult(Activity.RESULT_OK, it)
            }

            finish()
        }
    }
}