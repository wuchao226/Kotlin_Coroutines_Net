package com.wuc.kotlin_coroutines_net.login

import com.wuc.kotlin_coroutines_net.bean.User
import com.wuc.kotlin_coroutines_net.bean.WxArticleBean
import com.wuc.kotlin_coroutines_net.net.ApiService
import com.wuc.kotlin_coroutines_net.net.RetrofitClient
import com.wuc.kotlin_coroutines_net.net.RetrofitClient.service
import com.wuc.kotlin_coroutines_net.net.UserApi
import com.wuc.network.ApiResponse
import com.wuc.network.base.BaseRepository

/**
 * @author : wuchao5
 * @date : 2021/10/19 11:23
 * @desciption :
 */
class LoginRepository : BaseRepository() {
  private val mService by lazy { service }

  suspend fun login(username: String, password: String): ApiResponse<User?> {
    return executeHttp { mService.login(username, password) }
  }

  suspend fun fetchWxArticleFromNet(): ApiResponse<List<WxArticleBean>> {
    return executeHttp {
      mService.getWxArticle()
    }
  }

  suspend fun fetchWxArticleError(): ApiResponse<List<WxArticleBean>> {
    return executeHttp {
      mService.getWxArticleError()
    }
  }
}