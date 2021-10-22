package com.wuc.base.util

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View

/**
 * @author : wuchao5
 * @date : 2021/10/21 16:33
 * @desciption :
 */

inline fun <reified T : Activity> Activity.startActivity(bundle: Bundle? = null) {
  val intent = Intent(this, T::class.java)
  if (bundle != null) {
    intent.putExtras(bundle)
  }
  startActivity(intent)
}


/**
 * 防止连续点击
 */
fun View.clickWithLimit(intervalMill: Int = 500, block: ((v: View?) -> Unit)) {
  setOnClickListener(object : View.OnClickListener {
    var last = 0L
    override fun onClick(v: View?) {
      if (System.currentTimeMillis() - last > intervalMill) {
        block(v)
        last = System.currentTimeMillis()
      }
    }
  })
}