package com.example.a99password.presenter

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a99password.databinding.FragmentHomeBinding
import com.example.a99password.presenter.MenuActivity
import com.example.a99password.presenter.adapter.OptionsAdapter
import com.example.a99password.presenter.adapter.OptionsInterface
import android.R
import android.content.Context
import android.transition.TransitionInflater

import androidx.appcompat.app.AppCompatActivity
import com.example.a99password.presenter.RegisterUser.Companion.SHARED_PICTURE_ID
import com.example.a99password.presenter.RegisterUser.Companion.SHARED_PREFERENCES
import com.example.a99password.presenter.RegisterUser.Companion.SHARED_USERNAME
import android.content.Intent
import android.net.Uri


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), OptionsInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        val inflater = TransitionInflater.from(requireContext());
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
        exitTransition = inflater.inflateTransition(R.transition.slide_left)
    }

    private lateinit var binding : FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initData()
        initRecycler()
        initClicks()

        return binding.root
    }

    private fun initClicks() {
        binding.ivAssinatura.setOnClickListener {
            Intent().let { intent ->
                intent.action = Intent.ACTION_VIEW
                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                intent.data = Uri.parse("https://github.com/CardosofGui")
                startActivity(intent)
            }
        }
    }

    private fun initData() {
        val sharedPref = activity?.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val name = sharedPref?.getString(SHARED_USERNAME, "Null")
        val drawableID = sharedPref?.getInt(SHARED_PICTURE_ID, -1)

        binding.tvNameUser.setText("Continue protegendo suas senhas conosco $name!")
        binding.civUserIcon.setImageResource(drawableID!!)
    }

    private fun initRecycler(){
        val adapter = OptionsAdapter(this)
        binding.rcvOptions.adapter = adapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun initOption(fragment: Fragment, title : String) {
        (activity as MenuActivity?)?.switchFragment(fragment, title)
    }
}