package com.wuc.network.entity

import android.util.Log
import com.google.gson.JsonParseException
import com.wuc.network.toast
import retrofit2.HttpException
import java.net.SocketTimeoutException

/**
 * @author : wuchao5
 * @date : 2021/10/15 10:42
 * @desciption : 异常处理
 */
enum class HttpError(var code: Int, var errorMsg: String) {
  TOKEN_EXPIRE(3001, "token is expired"),
  PARAMS_ERROR(4003, "params is error")
}

/**
 * 业务逻辑异常给用户展示错误信息
 */
internal fun handlingApiExceptions(errorCode: Int?, msg: String?) {
  when (errorCode) {
    HttpError.TOKEN_EXPIRE.code -> toast(HttpError.TOKEN_EXPIRE.errorMsg)
    HttpError.PARAMS_ERROR.code -> toast(HttpError.PARAMS_ERROR.errorMsg)
    else -> msg?.let { toast(it) }
  }
}

/**
 * 非后台返回错误，捕获到的异常
 */
internal fun handlingExceptions(throwable: Throwable) {
  Log.e("error", throwable.message ?: "throwwwwwwww")
  when (throwable) {
    is HttpException -> {
      // ToastUtils.showShort(throwable.message())
      toast(throwable.message())
    }
    is SocketTimeoutException -> {
      // ToastUtils.showShort(UiUtils.getString(R.string.error_socket_timeout))
      toast("网络超时")
    }
    is JsonParseException -> {
      // ToastUtils.showShort(UiUtils.getString(R.string.error_data_parse))
      toast("数据解析异常")
    }
    else -> {
      // ToastUtils.showShort(UiUtils.getString(R.string.error_unkown))
      toast("未知错误")
    }
  }
}
