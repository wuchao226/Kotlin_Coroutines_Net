package com.wuc.kotlin_coroutines_net.bean

class WxArticleBean {

  var id = 0
  var name: String? = null
  var visible = 0

  override fun toString(): String {
    return "TestBean(id=$id, name=$name, visible=$visible)"
  }
}