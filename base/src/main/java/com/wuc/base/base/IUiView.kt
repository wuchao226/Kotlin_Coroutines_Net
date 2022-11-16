package com.wuc.base.base

import androidx.lifecycle.LifecycleOwner

/**
 * @author : wuchao5
 * @date : 2022/11/15 17:26
 * @desciption :
 */
interface IUiView : LifecycleOwner {
  fun showLoading()
  fun dismissLoading()
}