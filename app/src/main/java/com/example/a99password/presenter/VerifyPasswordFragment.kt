package com.example.a99password.presenter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.a99password.R
import com.example.a99password.databinding.FragmentVerifyPasswordBinding
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
 * Use the [VerifyPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VerifyPasswordFragment : Fragment(), PasswordInterface {
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

    private lateinit var binding : FragmentVerifyPasswordBinding


    private val passwordViewModel : PasswordViewModel by viewModels {
        PasswordViewModelFactory((activity?.application as BaseApplication).repository)
    }

    private lateinit var passwordAdapter : PasswordAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentVerifyPasswordBinding.inflate(inflater, container, false)

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
        passwordAdapter = PasswordAdapter(arrayListOf(), this, passwordVerify = true, editItemEnable = false)
        binding.rcvPasswords.adapter = passwordAdapter

        passwordViewModel.getAllPassword("").observe(this, { listPassword ->
            passwordAdapter.submitList(listPassword)
        })
    }

    override fun copyToClipboard(textToCopy : String) {
        val clipboardManager = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(textToCopy, textToCopy)
        clipboardManager.setPrimaryClip(clipData)

        FancyToast.makeText(requireContext(),"Senha copiada!", FancyToast.LENGTH_LONG, FancyToast.INFO,false).show()
    }

    override fun clickPassword(password: Password, statusSelected: Boolean): Boolean? = null

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VerifyPasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VerifyPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun editItem(password: Password?, groupPassword: GroupPassword?) {}

    override fun deleteItem(id: Int) {}
}