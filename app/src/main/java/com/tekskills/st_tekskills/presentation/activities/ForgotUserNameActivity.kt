package com.tekskills.st_tekskills.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.tekskills.st_tekskills.R
import com.tekskills.st_tekskills.databinding.ActivityForgotUsernameBinding
import com.tekskills.st_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.st_tekskills.utils.Common
import com.tekskills.st_tekskills.utils.RestApiStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotUserNameActivity : AppCompatActivity() {

    lateinit var binding: ActivityForgotUsernameBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_username)

        setupListeners()
        if (viewModel.checkIfUserLogin() != Common.PREF_DEFAULT) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        viewModel.resUserForgotUserName.observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.VISIBLE
                    if (it.data != null)
                        it.data.let { res ->
                            Snackbar.make(
                                binding.root,
                                "Response: ${res.message}",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            if (res.message == "User-Name Send To Email") {
                                onBackPressed()
                            }
                        }
                    else {
                        Snackbar.make(
                            binding.root,
                            "Forgot UserName Failed",
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

        })

        binding.btnSubmit.setOnClickListener {
            if (isValidate()) {
                binding.progress.visibility = View.VISIBLE
                viewModel.userForgotUserName(
                    binding.edtUsername.text.toString()
                )
            }
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun isValidate(): Boolean {
        var isValid = true
        if (!validateUserName()) isValid = false
        return isValid
    }

    private fun setupListeners() {
        binding.edtUsername.addTextChangedListener(TextFieldValidation(binding.edtUsername))
    }

    /**
     * field must not be empty
     */
    private fun validateUserName(): Boolean {
        val email = binding.edtUsername.text.toString().trim()

        if (email.isEmpty()) {
            binding.edtLoginUserName.error = "Required Field!"
            binding.edtUsername.requestFocus()
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtLoginUserName.error = "Invalid Email Address!"
            binding.edtUsername.requestFocus()
            return false
        } else {
            binding.edtLoginUserName.isErrorEnabled = false
        }
        return true
    }
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
     * applying text watcher on each text field
     */
    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // checking ids of each text field and applying functions accordingly.
            when (view.id) {
                R.id.edt_username -> {
                    validateUserName()
                }
            }
        }
    }
}