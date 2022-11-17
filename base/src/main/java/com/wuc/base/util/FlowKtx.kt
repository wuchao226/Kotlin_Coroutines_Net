package com.wuc.base.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.flowWithLifecycle
import com.wuc.base.base.IUiView
import com.wuc.network.ApiResponse
import com.wuc.network.ResultBuilder
import com.wuc.network.parseData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * @author : wuchao5
 * @date : 2022/11/15 19:52
 * @desciption :
 */
fun <T> launchFlow(
  resquestBlock: suspend () -> ApiResponse<T>,
  startCallback: (() -> Unit)? = null,
  completeCallback: (() -> Unit)? = null
): Flow<ApiResponse<T>> {
  return flow {
    emit(resquestBlock())
  }.onStart {
    startCallback?.invoke()
  }.onCompletion {
    completeCallback?.invoke()
  }
}

/**
 * 这个方法只是简单的一个封装Loading的普通方法，不返回任何实体类
 */
fun IUiView.launchWithLoading(resquestBlock: suspend () -> Unit) {
  lifecycleScope.launch {
    flow {
      emit(resquestBlock())
    }.onStart {
      showLoading()
    }.onCompletion {
      dismissLoading()
    }.collect()
  }
}

/**
 * 请求不带Loading&&不需要声明LiveData
 */
fun <T> IUiView.launchAndCollect(
  resquestBlock: suspend () -> ApiResponse<T>,
  listenerBuilder: ResultBuilder<T>.() -> Unit
) {
  lifecycleScope.launch {
    launchFlow(resquestBlock)
      .collect { apiResponse: ApiResponse<T> ->
        apiResponse.parseData(listenerBuilder)
      }
  }
}

fun <T> IUiView.launchWithLoadingAndCollect(
  resquestBlock: suspend () -> ApiResponse<T>,
  listenerBuilder: ResultBuilder<T>.() -> Unit
) {
  lifecycleScope.launch {
    launchFlow(resquestBlock, { showLoading() }, { dismissLoading() })
      .collect { apiResponse: ApiResponse<T> ->
        apiResponse.parseData(listenerBuilder)
      }
  }
}

/**
 * 用感知生命周期的方式收集流
 */
fun <T> Flow<ApiResponse<T>>.collectIn(
  lifecycleOwner: LifecycleOwner,
  minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
  listenerBuilder: ResultBuilder<T>.() -> Unit
): Job = lifecycleOwner.lifecycleScope.launch {
  flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState)
    .collect { apiResponse: ApiResponse<T> ->
      apiResponse.parseData(listenerBuilder)
    }
}
