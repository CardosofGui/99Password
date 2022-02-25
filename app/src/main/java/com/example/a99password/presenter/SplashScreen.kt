package com.example.a99password.presenter

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.a99password.databinding.SplashScreenBinding

class SplashScreen : AppCompatActivity() {

    private val binding by lazy { SplashScreenBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(verifyUsername()) {
            binding.loading.visibility = View.VISIBLE
            binding.btnStart.visibility = View.GONE

            Handler().postDelayed(Runnable {
                run {
                    startActivity(Intent(this, MenuActivity::class.java))
                    finish()
                }
            }, 5000)
        } else {
            binding.loading.visibility = View.GONE
            binding.btnStart.visibility = View.VISIBLE
        }


        initClicks()
    }

    private fun verifyUsername() : Boolean {
        val sharedPref = getSharedPreferences(RegisterUser.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val name = sharedPref?.getString(RegisterUser.SHARED_USERNAME, "Null")
        val drawableID = sharedPref?.getInt(RegisterUser.SHARED_PICTURE_ID, -1)

        return !(name == "Null" || drawableID == -1)
    }

    private fun initClicks() {
        binding.btnStart.setOnClickListener {
            val intent = Intent(this, RegisterUser::class.java)
            startActivity(intent)
        }
    }
}