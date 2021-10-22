package com.wuc.kotlin_coroutines_net.login

import com.wuc.kotlin_coroutines_net.bean.User
import com.wuc.kotlin_coroutines_net.net.UserApi
import com.wuc.network.ApiResponse
import com.wuc.network.BaseRepository
import com.wuc.network.RetrofitClient

/**
 * @author : wuchao5
 * @date : 2021/10/19 11:23
 * @desciption :
 */
class LoginRepository : BaseRepository() {
  private val service by lazy { getService(UserApi::class.java) }

  suspend fun login(username: String, password: String): ApiResponse<User?> {
    return executeHttp { service.login(username, password) }
  }
}