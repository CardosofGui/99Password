package com.example.a99password.presenter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.edit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a99password.R
import com.example.a99password.databinding.ActivityRegisterUserBinding
import com.example.a99password.presenter.adapter.PictureAdapter
import com.example.a99password.presenter.adapter.PictureInterface
import com.shashank.sony.fancytoastlib.FancyToast
import de.hdodenhof.circleimageview.CircleImageView

class RegisterUser : AppCompatActivity(), PictureInterface {

    private val binding by lazy { ActivityRegisterUserBinding.inflate(layoutInflater) }
    private lateinit var adapter : PictureAdapter
    private var selectedPicture = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRecycler()
        initClicks()
    }

    private fun initClicks() {
        binding.btnRegister.setOnClickListener {
            if(selectedPicture == -1 || binding.tieNameUser.text.isNullOrEmpty()){
                FancyToast.makeText(this,"Preencha todos os campos!", FancyToast.LENGTH_LONG, FancyToast.INFO,false).show()
            } else {
                val username = binding.tieNameUser.text.toString()

                val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)

                sharedPreferences.edit {
                    putString(SHARED_USERNAME, username)
                    putInt(SHARED_PICTURE_ID, selectedPicture)
                    commit()
                }

                Intent(this, MenuActivity::class.java).let {
                    startActivity(it)
                    finish()
                }

                FancyToast.makeText(this,"Usuario cadastrado com sucesso!",FancyToast.LENGTH_LONG, FancyToast.SUCCESS,false).show()
            }
        }

        binding.ibBackButton.setOnClickListener {
            finish()
        }
    }

    private fun initRecycler() {
        adapter = PictureAdapter(this, this)
        binding.rcvPictureUser.adapter = adapter
    }

    override fun selectPicture(drawableID: Int) {
        selectedPicture = drawableID
    }

    companion object {
        const val SHARED_USERNAME = "com.example.a99password.SHARED_USERNAME"
        const val SHARED_PICTURE_ID = "com.example.a99password.SHARED_PICTURE_ID"
        const val SHARED_PREFERENCES = "com.example.a99password.SHARED_PREFERENCE_USER"
    }
}