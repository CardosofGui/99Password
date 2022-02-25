package com.example.a99password.presenter

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import com.example.a99password.R
import com.example.a99password.databinding.FragmentAllPasswordsBinding
import com.example.a99password.databinding.FragmentHomeBinding
import com.example.a99password.domain.GroupPassword
import com.example.a99password.domain.Password
import com.example.a99password.framework.BaseApplication
import com.example.a99password.framework.viewmodel.PasswordViewModel
import com.example.a99password.framework.viewmodel.PasswordViewModelFactory
import com.example.a99password.presenter.adapter.PasswordAdapter
import com.example.a99password.presenter.adapter.PasswordInterface
import com.shashank.sony.fancytoastlib.FancyToast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [allPasswords.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllPasswordsFragment : Fragment(), PasswordInterface {
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

    private val passwordViewModel : PasswordViewModel by viewModels {
        PasswordViewModelFactory((activity?.application as BaseApplication).repository)
    }

    private lateinit var passwordAdapter : PasswordAdapter
    private lateinit var binding : FragmentAllPasswordsBinding
    private var insertPasswordRequestCode = 1001
    private var updatePasswordRequestCode = 1002

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAllPasswordsBinding.inflate(inflater, container, false)

        initClicks()
        initRecycler()
        initSearch()


        return binding.root
    }

    private fun initSearch() {
        binding.tieSearchPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val search = binding.tieSearchPassword.text.toString()

                passwordViewModel.getAllPassword(search).observe(requireActivity(), { listPassword ->
                    passwordAdapter.submitList(listPassword)
                })
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun initRecycler() {
        passwordAdapter = PasswordAdapter(arrayListOf(), this, passwordVerify = false, editItemEnable = true)
        binding.rcvPasswords.adapter = passwordAdapter

        passwordViewModel.getAllPassword("").observe(this, { listPassword ->
            passwordAdapter.submitList(listPassword)
        })
    }

    private fun initClicks() {
        binding.fabCreatePassword.setOnClickListener {
            Intent(requireContext(), RegisterPassword::class.java).let {
                it.putExtra("requestCode", insertPasswordRequestCode)
                startActivityForResult(it, insertPasswordRequestCode)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val idPassword = data?.getIntExtra(PASSWORD_ID, -1)
        val name = data?.getStringExtra(NAME_PASSWORD)
        val email = data?.getStringExtra(EMAIL_PASSWORD)
        val password = data?.getStringExtra(PASSWORD_PASSWORD)

        if(resultCode == Activity.RESULT_OK && requestCode == insertPasswordRequestCode){
            val passwordNew = Password(null, name!!, email!!, password!!)

            passwordViewModel.insertPassword(passwordNew)
        }

        if(resultCode == Activity.RESULT_OK && requestCode == updatePasswordRequestCode){
            Toast.makeText(requireContext(), "Chegou aqui fi", Toast.LENGTH_LONG).show()

            val password = Password(idPassword, name!!, email!!, password!!)

            passwordViewModel.updatePassword(password)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val NAME_PASSWORD = "NAME_PASSWORD"
        const val EMAIL_PASSWORD = "EMAIL_PASSWORD"
        const val PASSWORD_PASSWORD = "PASSWORD_PASSWORD"
        const val PASSWORD_ID = "PASSWORD_ID"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment allPasswords.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AllPasswordsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun copyToClipboard(textToCopy : String) {
        val clipboardManager = activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(textToCopy, textToCopy)
        clipboardManager.setPrimaryClip(clipData)

        FancyToast.makeText(requireContext(),"Senha copiada!", FancyToast.LENGTH_LONG, FancyToast.INFO,false).show()
    }

    override fun clickPassword(password: Password, statusSelected: Boolean): Boolean? = null

    override fun editItem(password: Password?, groupPassword: GroupPassword?) {
        Intent(requireContext(), RegisterPassword::class.java).let {
            it.putExtra(PASSWORD_ID, password?.id)
            it.putExtra(NAME_PASSWORD, password?.passwordName)
            it.putExtra(EMAIL_PASSWORD, password?.passwordEmail)
            it.putExtra(PASSWORD_PASSWORD, password?.passwordPassword)
            it.putExtra("requestCode", updatePasswordRequestCode)

            startActivityForResult(it, updatePasswordRequestCode)
        }
    }

    override fun deleteItem(id: Int) {
        passwordViewModel.deletePassword(id)
    }
}