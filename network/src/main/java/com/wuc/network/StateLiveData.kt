package com.wuc.network

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * @author : wuchao5
 * @date : 2022/11/15 16:45
 * @desciption : 扩展 LiveData，通过 kotlin 的 DSL 表达式替换 java 的 callback 回调，简写代码
 */
typealias StateMutableLiveData<T> = MutableLiveData<ApiResponse<T>>

@MainThread
fun <T> StateMutableLiveData<T>.observerState(
  owner: LifecycleOwner,
  listenerBuilder: ResultBuilder<T>.() -> Unit
) {
  observe(owner) { apiResponse -> apiResponse.parseData(listenerBuilder) }
}

@MainThread
fun <T> LiveData<ApiResponse<T>>.observerState(
  owner: LifecycleOwner,
  listenerBuilder: ResultBuilder<T>.() -> Unit
) {
  observe(owner) { apiResponse -> apiResponse.parseData(listenerBuilder) }
}