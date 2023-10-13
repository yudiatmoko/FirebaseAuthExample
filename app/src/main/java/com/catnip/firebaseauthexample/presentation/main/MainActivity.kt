package com.catnip.firebaseauthexample.presentation.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.catnip.firebaseauthexample.R
import com.catnip.firebaseauthexample.data.network.firebase.auth.FirebaseAuthDataSourceImpl
import com.catnip.firebaseauthexample.data.repository.UserRepositoryImpl
import com.catnip.firebaseauthexample.databinding.ActivityMainBinding
import com.catnip.firebaseauthexample.presentation.login.LoginActivity
import com.catnip.firebaseauthexample.utils.GenericViewModelFactory
import com.catnip.firebaseauthexample.utils.proceedWhen
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainViewModel by viewModels {
        GenericViewModelFactory.create(createViewModel())
    }

    private fun createViewModel(): MainViewModel {
        val firebaseAuth = FirebaseAuth.getInstance()
        val dataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val repo = UserRepositoryImpl(dataSource)
        return MainViewModel(repo)
    }

    //todo : create media picker result

    private fun changePhotoProfile(uri: Uri) {
        //todo : change photo profile here
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupForm()
        showUserData()
        setClickListeners()
        observeData()
    }

    private fun setClickListeners() {
        //todo : set click listener
        binding.tvChangePwd.setOnClickListener{
            requestChangePassword()
        }

        binding.btnChangeProfile.setOnClickListener {
            changeProfileData()
        }

        binding.tvLogout.setOnClickListener {
            doLogout()
        }
    }

    private fun requestChangePassword() {
        //todo : request change password to viewmodel and show dialog
        viewModel.createChangePasswordReq()
        AlertDialog.Builder(this)
            .setMessage("Change password request sent to your email" +
                    " ${viewModel.getCurrentUser()?.email}")
            .setPositiveButton("OK"){_,_ ->

            }.create().show()
    }

    private fun doLogout() {
        //todo :  show dialog, if yes proceed logout
        AlertDialog.Builder(this)
            .setMessage("Do you want to logout? " +
                    "${viewModel.getCurrentUser()?.fullName}")
            .setPositiveButton("Yes"){_,_ ->
                viewModel.doLogout()
                navigateToLogin()
            }.setNegativeButton("No"){_,_ ->

            }.create().show()
    }

    private fun navigateToLogin() {
        //todo :  navigate to login
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun changeProfileData() {
        //todo :  change fullname data
        if (isFormValid()){
            val fullName = binding.layoutForm.etName.text.toString().trim()
            viewModel.updateProfile(fullName)
        }
    }

    private fun isFormValid(): Boolean {
        //todo : create result from email validation and password
        val currentName = viewModel.getCurrentUser()?.fullName
        val newName = binding.layoutForm.etName.text.toString().trim()
        return checkNameValidation(currentName,newName)
    }

    private fun checkNameValidation(currentName: String?, newName: String): Boolean {
        //todo :  check if name is valid
        return if(newName.isEmpty()){
            binding.layoutForm.tilName.isErrorEnabled = true
            binding.layoutForm.tilName.error = getString(R.string.text_error_name_cannot_empty)
            false
        } else if (newName == currentName){
            binding.layoutForm.tilName.isErrorEnabled = true
            binding.layoutForm.tilName.error = getString(R.string.text_error_new_name_must_be_different)
            false
        } else{
            binding.layoutForm.tilName.isErrorEnabled = false
            true
        }
    }

    private fun observeData() {
        //todo : observe result change photo and detail
        viewModel.updateProfileResult.observe(this){
            it.proceedWhen(
                doOnSuccess = {
                    binding.pbLoading.isVisible = false
                    binding.btnChangeProfile.isVisible = true
                    binding.btnChangeProfile.isEnabled = true
                    Toast.makeText(
                        this,
                        "Change Profile Success",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                doOnLoading = {
                    binding.pbLoading.isVisible = true
                    binding.btnChangeProfile.isVisible = false
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnChangeProfile.isVisible = true
                    binding.btnChangeProfile.isEnabled = true
                    Toast.makeText(
                        this,
                        "Change Profile Failed: ${it.exception?.message.orEmpty()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    private fun setupForm() {
        //todo : setup form that required in this page
        binding.layoutForm.tilName.isVisible = true
        binding.layoutForm.tilEmail.isVisible = true
        binding.layoutForm.etEmail.isEnabled = false
    }

    private fun showUserData() {
        //todo : show user data to the views
        binding.layoutForm.etName.setText(viewModel.getCurrentUser()?.fullName)
        binding.layoutForm.etEmail.setText(viewModel.getCurrentUser()?.email)
    }
}