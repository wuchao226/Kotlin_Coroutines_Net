package com.wuc.kotlin_coroutines_net.net

import com.wuc.network.base.BaseRetrofitClient
import okhttp3.OkHttpClient

/**
 * @author : wuchao5
 * @date : 2022/11/16 14:13
 * @desciption :
 */
object RetrofitClient : BaseRetrofitClient() {
  val service by lazy { getService(ApiService::class.java) }
  override fun handleBuilder(builder: OkHttpClient.Builder) = Unit
}