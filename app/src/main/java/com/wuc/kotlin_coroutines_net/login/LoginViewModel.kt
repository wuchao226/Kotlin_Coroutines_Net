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
import com.wuc.network.guardSuccess
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

      // getOrNull 常用于后接一个 ?.let 只处理成功情况；
      // 如果失败的情况需要做些动作，则需用 guardSuccess
      // Nothing 是一个 空类型
      val user: User? = reponse.login(username, password)
        .guardSuccess {
          // 网络请求不是 ApiSuccessResponse 的业务逻辑处理
          // ...
          return@launch
        }
      // ...
      // 拿到非 null 的 User 继续后面的业务逻辑

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