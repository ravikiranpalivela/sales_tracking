package com.tekskills.st_tekskills.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tekskills.st_tekskills.R
import com.tekskills.st_tekskills.databinding.ActivityChangePasswordBinding
import com.tekskills.st_tekskills.presentation.activities.MainActivity
import com.tekskills.st_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.st_tekskills.utils.RestApiStatus

class ChangePasswordFragment : Fragment() {
    lateinit var binding: ActivityChangePasswordBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.activity_change_password, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        setupListeners()
        viewModel.resUserChangePassword.observe(viewLifecycleOwner) {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.VISIBLE
                    if (it.data != null)
                        it.data.let { res ->
                            if(res.message == "success")
                            {
                                (activity as MainActivity?)!!.logOutClearData()
//                                requireActivity().onBackPressed()
                            }
                        }
                    else {
                        Snackbar.make(
                            binding.root,
                            "Login Failed",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    binding.progress.visibility = View.GONE
                }

                RestApiStatus.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }

                RestApiStatus.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

        }

        binding.btnSubmit.setOnClickListener {
            if (isValidate()) {
                binding.progress.visibility = View.VISIBLE
                viewModel.userChangePassword(
                    binding.newPassword.text.toString()
                )
            }
        }
    }

    private fun isValidate(): Boolean {
        var isValid = true
        if (!validatePassword()) isValid = false
        if (!validateConfirmPassword()) isValid = false
        return isValid
    }

    private fun setupListeners() {
        binding.newPassword.addTextChangedListener(TextFieldValidation(binding.newPassword))
        binding.confirmPassword.addTextChangedListener(TextFieldValidation(binding.confirmPassword))
        binding.edtUsername.setText(viewModel.getUserName())
    }

    /**
     * field must not be empty
     */
//    private fun validateUserName(): Boolean {
//        if (binding.edtUsername.text.toString().trim().isEmpty()) {
//            binding.edtLoginUserName.error = "Required Field!"
//            binding.edtUsername.requestFocus()
//            return false
//        } else {
//            binding.edtLoginUserName.isErrorEnabled = false
//        }
//        return true
//    }

    /**
     * 1) field must not be empty
     * 2) password lenght must not be less than 6
     * 3) password must contain at least one digit
     * 4) password must contain atleast one upper and one lower case letter
     * 5) password must contain atleast one special character.
     */
    private fun validatePassword(): Boolean {
        if (binding.newPassword.text.toString().trim().isEmpty()) {
            binding.edtNewPassword.error = "Required Field!"
            binding.newPassword.requestFocus()
            return false
        } else if (binding.newPassword.text.toString().length < 6) {
            binding.edtNewPassword.error = "password can't be less than 6"
            binding.newPassword.requestFocus()
            return false
        } else {
            binding.edtNewPassword.isErrorEnabled = false
        }
        return true
    }

    /**
     * 1) field must not be empty
     * 2) password lenght must not be less than 6
     * 3) password must contain at least one digit
     * 4) password must contain atleast one upper and one lower case letter
     * 5) password must contain atleast one special character.
     */
    private fun validateConfirmPassword(): Boolean {
        if (binding.confirmPassword.text.toString().trim().isEmpty()) {
            binding.edtConfirmPassword.error = "Required Field!"
            binding.confirmPassword.requestFocus()
            return false
        } else if (binding.confirmPassword.text.toString().length < 6) {
            binding.edtConfirmPassword.error = "password can't be less than 6"
            binding.confirmPassword.requestFocus()
            return false
        } else {
            if (binding.confirmPassword.text.toString()
                    .trim() != binding.newPassword.text.toString().trim()
            ) {
                binding.edtConfirmPassword.error = "Password does not match!"
                binding.confirmPassword.requestFocus()
                return false
            } else {
                binding.edtConfirmPassword.isErrorEnabled = false
            }
        }
        return true
    }

    /**
     * applying text watcher on each text field
     */
    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // checking ids of each text field and applying functions accordingly.
            when (view.id) {
                R.id.confirm_password -> {
                    validateConfirmPassword()
                }

                R.id.new_password -> {
                    validatePassword()
                }
            }
        }
    }

    companion object {
        fun newInstance(): ChangePasswordFragment {
            return ChangePasswordFragment()
        }
    }
}