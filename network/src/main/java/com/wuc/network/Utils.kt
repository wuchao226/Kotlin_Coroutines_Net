package com.wuc.network

import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * @author : wuchao5
 * @date : 2021/10/20 16:20
 * @desciption :
 */
const val SHOW_TOAST = "show_toast"

// const val LOADING_STATE = "loading_state"

internal fun toast(msg: String) {
  LiveEventBus.get<String>(SHOW_TOAST).post(msg)
}