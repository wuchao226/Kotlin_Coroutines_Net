package com.wuc.base.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wuc.network.ApiResponse
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * @author : wuchao5
 * @date : 2021/10/20 16:17
 * @desciption :
 */
open class BaseViewModel : ViewModel() {

  /**
   * 显示加载框
   */
  // private fun showLoading() {
  //   // LiveEventBus.get<Boolean>(LOADING_STATE).post(true)
  // }
  //
  // /**
  //  * 隐藏加载框
  //  */
  // private fun stopLoading() {
  //   // LiveEventBus.get<Boolean>(LOADING_STATE).post(false)
  // }

  /**
   * 展示 loading 的网络请求
   */
  /*protected fun <T> launchWithLoading(
    // 网络请求执行方法
    requestBlock: suspend () -> ApiResponse<T>,
    // 结果的回调
    resultCallback: (ApiResponse<T>) -> Unit
  ) {
    viewModelScope.launch {
      flow {
        emit(requestBlock.invoke())
      }.onStart {
        showLoading()
      }.onCompletion {
        stopLoading()
      }.collect {
        resultCallback(it)
      }
    }
  }*/
}