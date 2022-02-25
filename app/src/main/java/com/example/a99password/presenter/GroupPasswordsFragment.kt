package com.example.a99password.presenter

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import com.example.a99password.R
import com.example.a99password.databinding.FragmentGroupPasswordsBinding
import com.example.a99password.domain.GroupPassword
import com.example.a99password.domain.Password
import com.example.a99password.framework.BaseApplication
import com.example.a99password.framework.viewmodel.GroupPasswordViewModel
import com.example.a99password.framework.viewmodel.GroupPasswordViewModelFactory
import com.example.a99password.framework.viewmodel.PasswordViewModel
import com.example.a99password.framework.viewmodel.PasswordViewModelFactory
import com.example.a99password.presenter.adapter.GroupAdapter
import com.example.a99password.presenter.adapter.GroupInterface
import com.example.a99password.presenter.adapter.PasswordAdapter
import com.example.a99password.presenter.adapter.PasswordInterface
import com.shashank.sony.fancytoastlib.FancyToast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GroupPasswords.newInstance] factory method to
 * create an instance of this fragment.
 */
class GroupPasswordsFragment : Fragment(), GroupInterface, PasswordInterface {
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

    private var listPasswordAll = arrayListOf<Password>()
    private var filteredListPassword = arrayListOf<Password>()

    private lateinit var passwordAdapter : PasswordAdapter
    private lateinit var groupAdapter: GroupAdapter

    private val groupPasswordViewModel : GroupPasswordViewModel by viewModels {
        GroupPasswordViewModelFactory((activity?.application as BaseApplication).repository)
    }

    private lateinit var binding : FragmentGroupPasswordsBinding

    private var insertGroupPasswordRequestCode = 2001
    private var updateGroupPasswordRequestCode = 2002

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGroupPasswordsBinding.inflate(inflater, container, false)

        initClick()
        initRecycler()
        initSearch()

        return binding.root
    }

    private fun initSearch() {
        binding.tieSearchGroupPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val search = binding.tieSearchGroupPassword.text.toString()

                groupPasswordViewModel.getListGroupPassword(search).observe(requireActivity(), { listPassword ->
                    groupAdapter.submitList(listPassword)
                })
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.tieSearchPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val search = binding.tieSearchPassword.text.toString()

                filteredListPassword = listPasswordAll.filter { it.passwordEmail.contains(search, ignoreCase = true) || it.passwordName.contains(search, ignoreCase = true) } as ArrayList<Password>

                passwordAdapter.submitList(filteredListPassword)
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val groupID = data?.getIntExtra(GROUP_ID, -1)
        val groupName = data?.getStringExtra(GROUP_NAME)
        val groupPasswordSelected = data?.getStringExtra(GROUP_PASSWORD_LIST)

        if(requestCode == insertGroupPasswordRequestCode && resultCode == Activity.RESULT_OK){
            val groupPassword = GroupPassword(null, groupName!!, groupPasswordSelected)

            groupPasswordViewModel.insertGroupPassword(groupPassword)
        }

        if(requestCode == updateGroupPasswordRequestCode && resultCode == Activity.RESULT_OK){
            val groupPassword = GroupPassword(groupID, groupName!!, groupPasswordSelected)

            groupPasswordViewModel.updateGroupPassword(groupPassword)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initClick() {
        binding.fabCreateGroupPassword.setOnClickListener {
            Intent(requireContext(), RegisterGroup::class.java).let {
                it.putExtra("requestCode", insertGroupPasswordRequestCode)
                startActivityForResult(it, insertGroupPasswordRequestCode)
            }
        }

        binding.ibBackButton.setOnClickListener {
            showGroupPassword()
        }
    }

    private fun initRecycler() {
        groupAdapter = GroupAdapter(arrayListOf(), this)
        binding.rcvGroupPassword.adapter = groupAdapter

        groupPasswordViewModel.getListGroupPassword("").observe(this, { groupListPassword ->
            groupAdapter.submitList(groupListPassword)
        })
    }

    companion object {
        const val GROUP_NAME = "com.example.a99password.GROUP_NAME"
        const val GROUP_PASSWORD_LIST = "com.example.a99password.GROUP_PASSWORD_LIST"
        const val GROUP_ID = "com.example.a99password.GROUP_ID"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GroupPasswords.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GroupPasswordsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun showGroupPassword() {
        val animationEnter = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_slide_in_right)

        animationEnter.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                binding.rcvPasswords.visibility = View.INVISIBLE
                binding.tilSearchPassword.visibility = View.INVISIBLE
                binding.ibBackButton.visibility = View.INVISIBLE
            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.rcvGroupPassword.visibility = View.VISIBLE
                binding.tilSearchGroup.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(p0: Animation?) {}
        })

        binding.tilSearchGroup.startAnimation(animationEnter)
        binding.rcvGroupPassword.startAnimation(animationEnter)
    }

    private fun showPassword() {
        val animationEnter = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_slide_in_left)

        animationEnter.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                binding.rcvGroupPassword.visibility = View.INVISIBLE
                binding.tilSearchGroup.visibility = View.INVISIBLE
            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.rcvPasswords.visibility = View.VISIBLE
                binding.tilSearchPassword.visibility = View.VISIBLE
                binding.ibBackButton.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(p0: Animation?) {}
        })

        binding.tilSearchPassword.startAnimation(animationEnter)
        binding.rcvPasswords.startAnimation(animationEnter)
        binding.ibBackButton.startAnimation(animationEnter)
    }

    override fun expandGroupClicK(listPassword: List<Password>?) {
        if(listPassword == null){
            FancyToast.makeText(requireContext(),"Este grupo não possui nenhuma senha", FancyToast.LENGTH_LONG, FancyToast.INFO,false).show()
            return
        }

        listPasswordAll = listPassword as ArrayList<Password>

        passwordAdapter = PasswordAdapter(listPassword, this, passwordVerify = false, editItemEnable = false)
        binding.rcvPasswords.adapter = passwordAdapter

        showPassword()
    }

    override fun editGroupClick(groupPassword: GroupPassword) {
        Intent(requireContext(), RegisterGroup::class.java).let {
            it.putExtra(GROUP_ID, groupPassword.id)
            it.putExtra(GROUP_NAME, groupPassword.name)
            it.putExtra(GROUP_PASSWORD_LIST, groupPassword.passwords)
            it.putExtra("requestCode", updateGroupPasswordRequestCode)

            startActivityForResult(it, updateGroupPasswordRequestCode)
        }
    }

    override fun copyToClipboard(textToCopy: String) {
        val clipboardManager = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(textToCopy, textToCopy)
        clipboardManager.setPrimaryClip(clipData)

        FancyToast.makeText(requireContext(),"Senha copiada!", FancyToast.LENGTH_LONG, FancyToast.INFO,false).show()
    }

    override fun clickPassword(password: Password, statusSelected: Boolean): Boolean = false

    override fun editItem(password: Password?, groupPassword: GroupPassword?) {
        Intent(requireContext(), RegisterGroup::class.java).let {
            it.putExtra(GROUP_ID, groupPassword?.id)
            it.putExtra(GROUP_NAME, groupPassword?.name)
            it.putExtra(GROUP_PASSWORD_LIST, groupPassword?.passwords)
            it.putExtra("requestCode", updateGroupPasswordRequestCode)

            startActivityForResult(it, updateGroupPasswordRequestCode)
        }
    }

    override fun deleteItem(id: Int) {
        groupPasswordViewModel.deleteGroupPassword(id)
    }
}