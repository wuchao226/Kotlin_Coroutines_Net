package com.wuc.network.base

import com.wuc.network.*
import com.wuc.network.entity.handlingApiExceptions
import com.wuc.network.entity.handlingExceptions

/**
 * @author : wuchao5
 * @date : 2021/10/15 16:28
 * @desciption : 统一处理网络请求
 */
open class BaseRepository {
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

  /**
   * 返回200，但是还要判断isSuccess
   */
  private fun <T> handleHttpOk(data: ApiResponse<T>): ApiResponse<T> {
    return if (data.isSuccess) {
      getHttpSuccessResponse(data)
    } else {
      handlingApiExceptions(data.errorCode, data.errorMsg)
      ApiFailedResponse(data.errorCode, data.errorMsg)
    }
  }

  /**
   * 非后台返回错误，捕获到的异常
   */
  private fun <T> handleHttpError(e: Throwable): ApiErrorResponse<T> {
    if (BuildConfig.DEBUG) {
      e.printStackTrace()
    }
    handlingExceptions(e)
    return ApiErrorResponse(e)
  }

  /**
   * 成功和数据为空的处理
   */
  private fun <T> getHttpSuccessResponse(response: ApiResponse<T>): ApiResponse<T> {
    val data = response.data
    return if (data == null || data is List<*> && (data as List<*>).isEmpty()) {
      ApiEmptyResponse()
    } else {
      ApiSuccessResponse(data)
    }
  }
}