package com.wuc.kotlin_coroutines_net.bean


/**
 * @author : wuchao5
 * @date : 2021/10/15 16:54
 * @desciption :
 */
// @JsonClass(generateAdapter = true)
data class User(
  val admin: Boolean?,
  val chapterTops: List<Any>?,
  val email: String?,
  val icon: String?,
  val id: Int?,
  val nickname: String?,
  val publicName: String?,
  val username: String?
)
