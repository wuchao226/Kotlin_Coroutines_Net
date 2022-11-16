package com.wuc.base.base

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jeremyliao.liveeventbus.LiveEventBus
import com.wuc.base.util.toast
import com.wuc.network.SHOW_TOAST

/**
 * @author : wuchao5
 * @date : 2021/10/20 17:43
 * @desciption :
 */
open class BaseActivity : AppCompatActivity(), IUiView {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    observeUi()
  }

  private fun observeUi() {
    LiveEventBus.get<String>(SHOW_TOAST).observe(this) {
      toast(it)
    }
  }

  private var progressDialog: ProgressDialog? = null

  override fun showLoading() {
    if (progressDialog == null) {
      progressDialog = ProgressDialog(this)
    }
    progressDialog?.show()
  }

  override fun dismissLoading() {
    progressDialog?.takeIf { it.isShowing }?.dismiss()
  }
}