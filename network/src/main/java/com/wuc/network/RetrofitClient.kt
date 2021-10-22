package com.wuc.network

import android.util.Log
import com.wuc.network.exception.handleBizError
import com.wuc.network.exception.handleOtherError
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author : wuchao5
 * @date : 2021/10/14 11:47
 * @desciption :
 */
class RetrofitClient {

  companion object {
    // 双重校验 单例
    val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { RetrofitClient() }
    const val BASE_URL = "https://wanandroid.com/"
    const val TIME_OUT = 30
  }

  private val okHttpClient: OkHttpClient by lazy {
    OkHttpClient.Builder()
      .connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
      .readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
      .writeTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
      .addInterceptor(getHttpLoggingInterceptor())
      //默认重试一次，若需要重试N次，则要实现拦截器
      .retryOnConnectionFailure(true)
      .build()
  }

  private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor { log ->
      Log.i("retrofit", log)
    }
    if (BuildConfig.DEBUG) {
      logging.level = HttpLoggingInterceptor.Level.BODY
    } else {
      logging.level = HttpLoggingInterceptor.Level.BASIC
    }
    return logging
  }

  fun <T> getService(clazz: Class<T>): T {
    return getService(clazz, BASE_URL)
  }

  fun <T> getService(clazz: Class<T>, baseUrl: String): T {
    return Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(okHttpClient)
      // .addCallAdapterFactory(catchingCallAdapterFactory)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(clazz)
  }

}