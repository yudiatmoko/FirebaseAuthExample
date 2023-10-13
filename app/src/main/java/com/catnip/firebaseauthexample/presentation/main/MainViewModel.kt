package com.catnip.firebaseauthexample.presentation.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catnip.firebaseauthexample.data.repository.UserRepository
import com.catnip.firebaseauthexample.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class MainViewModel(private val repo: UserRepository) : ViewModel() {

    private val _updateProfileResult = MutableLiveData<ResultWrapper<Boolean>>()
    val updateProfileResult: LiveData<ResultWrapper<Boolean>>
        get() = _updateProfileResult

    fun getCurrentUser() = repo.getCurrentUser()
    fun createChangePasswordReq(){
        repo.sendChangePasswordRequestByEmail()
    }
    fun doLogout(){
        repo.doLogout()
    }
    fun updateProfile(fullName: String){
        viewModelScope.launch {
            repo.updateProfile(fullName).collect{
                _updateProfileResult.postValue(it)
            }
        }
    }
}