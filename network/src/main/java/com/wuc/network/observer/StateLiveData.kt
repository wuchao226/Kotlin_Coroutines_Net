package com.wuc.network.observer

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.wuc.network.ApiResponse
import com.wuc.network.toast

/**
 * @author : wuchao5
 * @date : 2021/10/21 15:26
 * @desciption : 扩展 LiveData，通过 kotlin 的 DSL 表达式替换 java 的 callback 回调，简写代码
 */
class StateLiveData<T> : MutableLiveData<ApiResponse<T>>() {
  fun observerState(owner: LifecycleOwner, listenerBuilder: ListenerBuilder.() -> Unit) {
    val listener = ListenerBuilder().also(listenerBuilder)
    val value = object : IStateObserver<T>() {
      override fun onSuccess(data: T?) {
        listener.mSuccessListenerAction?.invoke(data)
      }

      override fun onDataEmpty() {
        listener.mEmptyListenerAction?.invoke()
      }

      override fun onError(e: Throwable) {
        listener.mErrorListenerAction?.invoke(e) ?: toast("Http Error")
      }

      override fun onComplete() {
        listener.mCompleteListenerAction?.invoke()
      }

      override fun onFailed(errorCode: Int?, errorMsg: String?) {
        listener.mFailedListenerAction?.invoke(errorCode, errorMsg)
      }
    }
    super.observe(owner, value)
  }

  inner class ListenerBuilder {
    internal var mSuccessListenerAction: ((T?) -> Unit)? = null
    internal var mErrorListenerAction: ((Throwable) -> Unit)? = null
    internal var mEmptyListenerAction: (() -> Unit)? = null
    internal var mCompleteListenerAction: (() -> Unit)? = null
    internal var mFailedListenerAction: ((Int?, String?) -> Unit)? = null

    fun onSuccess(action: (T?) -> Unit) {
      mSuccessListenerAction = action
    }

    fun onFailed(action: (Int?, String?) -> Unit) {
      mFailedListenerAction = action
    }

    fun onException(action: (Throwable) -> Unit) {
      mErrorListenerAction = action
    }

    fun onEmpty(action: () -> Unit) {
      mEmptyListenerAction = action
    }

    fun onComplete(action: () -> Unit) {
      mCompleteListenerAction = action
    }
  }
}

