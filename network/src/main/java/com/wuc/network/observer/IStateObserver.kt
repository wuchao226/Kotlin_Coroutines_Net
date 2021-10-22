package com.wuc.network.observer

import androidx.lifecycle.Observer
import com.wuc.network.*

/**
 * @author : wuchao5
 * @date : 2021/10/21 12:07
 * @desciption : 扩展 Observer，在 LiveData 的 Observer() 来判断是哪种数据类，进行相应的回调处理
 */
abstract class IStateObserver<T> : Observer<ApiResponse<T>> {
  override fun onChanged(apiResponse: ApiResponse<T>?) {
    when (apiResponse) {
      is ApiSuccessResponse -> onSuccess(apiResponse.data)
      is ApiBizError -> onFailed(apiResponse.errorCode, apiResponse.errorMsg)
      is ApiOtherError -> onError(apiResponse.throwable)
      is ApiEmptyResponse -> onDataEmpty()
    }
    onComplete()
  }

  abstract fun onSuccess(data: T?)

  abstract fun onDataEmpty()

  abstract fun onError(e: Throwable)

  abstract fun onComplete()

  abstract fun onFailed(errorCode: Int?, errorMsg: String?)
}