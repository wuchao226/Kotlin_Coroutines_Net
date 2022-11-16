package com.wuc.kotlin_coroutines_net.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wuc.base.base.BaseViewModel
import com.wuc.base.util.launchFlow
import com.wuc.kotlin_coroutines_net.bean.User
import com.wuc.kotlin_coroutines_net.bean.WxArticleBean
import com.wuc.network.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @author : wuchao5
 * @date : 2021/10/15 19:03
 * @desciption :
 */
class LoginViewModel : BaseViewModel() {
  private val repository by lazy { LoginRepository() }
  // 使用StateFlow 替代livedata
//    val wxArticleLiveData = StateMutableLiveData<List<WxArticleBean>>()

  private val _uiState = MutableStateFlow<ApiResponse<List<WxArticleBean>>>(ApiResponse())
  val uiState: StateFlow<ApiResponse<List<WxArticleBean>>> = _uiState.asStateFlow()

  suspend fun requestNet() {
    _uiState.value = repository.fetchWxArticleFromNet()
  }

  suspend fun requestNetError() {
    _uiState.value = repository.fetchWxArticleError()
  }

  /**
   * 场景：不需要监听数据变化
   */
  suspend fun login2(username: String, password: String): ApiResponse<User?> {
    return repository.login(username, password)
  }

  val userLiveData = StateMutableLiveData<User?>()

  fun login(username: String, password: String) {
    viewModelScope.launch {
      // 不需要loading
      repository.login(username, password)
        .getOrNull()
        ?.let {
          Log.i("user", it.toString())
        }

      // getOrNull 常用于后接一个 ?.let 只处理成功情况；
      // 如果失败的情况需要做些动作，则需用 guardSuccess
      // Nothing 是一个 空类型
      val user: User? = repository.login(username, password)
        .guardSuccess {
          // 网络请求不是 ApiSuccessResponse 的业务逻辑处理
          // ...
          return@launch
        }
      // ...
      // 拿到非 null 的 User 继续后面的业务逻辑
    }
  }
}