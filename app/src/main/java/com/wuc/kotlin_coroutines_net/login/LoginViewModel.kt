package com.wuc.kotlin_coroutines_net.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wuc.base.base.BaseViewModel
import com.wuc.kotlin_coroutines_net.bean.User
import com.wuc.network.ApiResponse
import com.wuc.network.getOrNull
import com.wuc.network.observer.StateLiveData
import kotlinx.coroutines.launch

/**
 * @author : wuchao5
 * @date : 2021/10/15 19:03
 * @desciption :
 */
class LoginViewModel : BaseViewModel() {
  private val reponse by lazy { LoginRepository() }

  val userLiveData = StateLiveData<User?>()


  fun login(username: String, password: String) {

    viewModelScope.launch {
      // 不需要loading
      reponse.login(username, password)
        .getOrNull()
        ?.let {
          Log.i("user", it.toString())
        }
      // 需要loading
      launchWithLoading(requestBlock = {
        reponse.login(username, password)
      }, resultCallback = {
        userLiveData.value = it
        it
          .getOrNull()
          ?.let { user ->
            Log.i("user", user.toString())
          }
      })
    }
  }
}