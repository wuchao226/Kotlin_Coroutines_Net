package com.wuc.network

/**
 * @author : wuchao5
 * @date : 2022/11/15 14:46
 * @desciption : 扩展 ApiResponse，在 ApiResponse 的 parseData() 来判断是哪种数据类，进行相应的回调处理
 */

fun <T> ApiResponse<T>.parseData(listenerBuilder: ResultBuilder<T>.() -> Unit) {
  val listener = ResultBuilder<T>().also(listenerBuilder)
  when (this) {
    is ApiSuccessResponse -> listener.onSuccess(this.data)
    is ApiEmptyResponse -> listener.onDataEmpty()
    is ApiFailedResponse -> listener.onFailed(this.errorCode, this.errorMsg)
    is ApiErrorResponse -> listener.onError(this.throwable)
  }
  listener.onComplete()
}

class ResultBuilder<T> {
  var onSuccess: (data: T?) -> Unit = {}
  var onDataEmpty: () -> Unit = {}
  var onFailed: (errorCode: Int?, errorMsg: String?) -> Unit = { _, errorMsg ->
    errorMsg?.let { toast(it) }
  }
  var onError: (e: Throwable) -> Unit = { e ->
    e.message?.let { toast(it) }
  }
  var onComplete: () -> Unit = {}
}