package com.wuc.kotlin_coroutines_net.net

import com.wuc.kotlin_coroutines_net.bean.User
import com.wuc.network.ApiResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author : wuchao5
 * @date : 2021/10/15 16:53
 * @desciption :
 */
interface UserApi {
  @FormUrlEncoded
  @POST("user/login")
  suspend fun login(
    @Field("username") userName: String,
    @Field("password") passWord: String
  ): ApiResponse<User?>
}