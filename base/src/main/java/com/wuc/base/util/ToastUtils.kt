package com.wuc.base.util

import android.content.Context
import android.widget.Toast

/**
 * @author : wuchao5
 * @date : 2021/10/20 15:50
 * @desciption :
 */
fun Context.toast(text: String) {
  Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}
fun Context.toast(resId: Int) {
  Toast.makeText(this, this.resources.getString(resId), Toast.LENGTH_SHORT).show()
}

fun Context.toastLong(text: String) {
  Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun Context.toastLong(resId: Int) {
  Toast.makeText(this, this.resources.getString(resId), Toast.LENGTH_LONG).show()
}