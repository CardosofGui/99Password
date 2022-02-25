package com.example.a99password.presenter

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.a99password.*
import com.example.a99password.databinding.ActivityMenuBinding
import org.w3c.dom.Text

class MenuActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMenuBinding.inflate(layoutInflater) }

    private val home = HomeFragment()

    private var lastTitle : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(savedInstanceState == null){
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, home)
                .commit()
        }

        initToolbar()
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun switchFragment(fragment : Fragment, title : String){
        if(fragment != HomeFragment()){
            binding.toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_baseline_arrow_back_ios_24)
        }

        binding.toolbar.title = title

        lastTitle = title

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack( "Tag" )
            .setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right, R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
            .commit()
     }

    override fun onBackPressed() {
        binding.toolbar.title = "Ínicio"
        binding.toolbar.navigationIcon = null

        super.onBackPressed()
    }
}