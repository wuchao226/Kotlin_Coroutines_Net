package com.wuc.network

import com.wuc.network.exception.handleBizError
import com.wuc.network.exception.handleOtherError

/**
 * @author : wuchao5
 * @date : 2021/10/15 16:28
 * @desciption : 统一处理网络请求
 */
open class BaseRepository {

  fun <T> getService(clazz: Class<T>): T {
    return RetrofitClient.instance.getService(clazz)
  }

  fun <T> getService(clazz: Class<T>, url: String): T {
    return RetrofitClient.instance.getService(clazz, url)
  }

  suspend fun <T> executeHttp(block: suspend () -> ApiResponse<T>): ApiResponse<T> {
    // val service = RetrofitClient.instance.getService(clazz)
    runCatching {
      block.invoke()
    }.onSuccess { data: ApiResponse<T> ->
      return handleHttpOk(data)
    }.onFailure { e ->
      return handleHttpError(e)
    }
    return ApiEmptyResponse()
  }

  private fun <T> handleHttpOk(data: ApiResponse<T>): ApiResponse<T> {
    return if (data.isSuccess) {
      getHttpSuccessResponse(data)
    } else {
      handleBizError(data.errorCode, data.errorMsg)
      ApiBizError(data.errorCode, data.errorMsg)
    }
  }

  /**
   * 非后台返回错误，捕获到的异常
   */
  private fun <T> handleHttpError(e: Throwable): ApiOtherError<T> {
    if (BuildConfig.DEBUG) {
      e.printStackTrace()
    }
    handleOtherError(e)
    return ApiOtherError(e)
  }

  private fun <T> getHttpSuccessResponse(response: ApiResponse<T>): ApiResponse<T> {
    val data = response.data
    return if (data == null || data is List<*> && (data as List<*>).isEmpty()) {
      ApiEmptyResponse()
    } else {
      ApiSuccessResponse(data)
    }
  }
}