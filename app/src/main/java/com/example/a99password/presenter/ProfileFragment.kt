package com.example.a99password.presenter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import com.example.a99password.R
import com.example.a99password.databinding.FragmentProfileBinding
import com.example.a99password.presenter.adapter.PictureAdapter
import com.example.a99password.presenter.adapter.PictureInterface
import com.shashank.sony.fancytoastlib.FancyToast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment(), PictureInterface {
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
        enterTransition = inflater.inflateTransition(android.R.transition.slide_right)
        exitTransition = inflater.inflateTransition(android.R.transition.slide_left)
    }

    private lateinit var binding : FragmentProfileBinding
    private var selectedPicture = -1
    private lateinit var adapter : PictureAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        initRecycler()
        setData()
        initClicks()

        return binding.root
    }

    private fun setData(){
        val sharedPref = activity?.getSharedPreferences(RegisterUser.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val name = sharedPref?.getString(RegisterUser.SHARED_USERNAME, "Null")
        val drawableID = sharedPref?.getInt(RegisterUser.SHARED_PICTURE_ID, -1)

        adapter.setPicture(drawableID!!)
        binding.tieNameUser.setText(name)
    }

    private fun initRecycler() {
        adapter = PictureAdapter(requireContext(), this)
        binding.rcvPictureUser.adapter = adapter
    }

    private fun initClicks() {
        binding.btnUpdate.setOnClickListener {
            if(selectedPicture == -1 || binding.tieNameUser.text.isNullOrEmpty()){
                FancyToast.makeText(requireContext(),"Preencha todos os campos!", FancyToast.LENGTH_LONG, FancyToast.INFO,false).show()
            } else {
                val username = binding.tieNameUser.text.toString()

                val sharedPreferences = activity?.getSharedPreferences(RegisterUser.SHARED_PREFERENCES, Context.MODE_PRIVATE)

                sharedPreferences?.edit {
                    putString(RegisterUser.SHARED_USERNAME, username)
                    putInt(RegisterUser.SHARED_PICTURE_ID, selectedPicture)
                    commit()
                }

                FancyToast.makeText(requireContext(),"Dados atualizados com sucesso!",
                    FancyToast.LENGTH_LONG, FancyToast.SUCCESS,false).show()
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun selectPicture(drawableID: Int) {
        selectedPicture = drawableID
    }
}