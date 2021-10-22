package com.wuc.network

import android.util.Log
import java.io.Serializable

/**
 * @author : wuchao5
 * @date : 2021/10/14 11:25
 * @desciption : 数据返回类,基于后台返回的基类，根据不同的结果，定义不同的状态数据类
 */
open class ApiResponse<T>(
  // 具体响应业务对象
  // open val data: T? = null,
  // 响应状态码
  // open val retcode: Int? = null,
  // 响应文字消息
  // open val retmsg: String? = null
  open val data: T? = null,
  open val errorCode: Int? = null,
  open val errorMsg: String? = null,
  open val error: Throwable? = null,
) : Serializable {
  // 请求是否成功
  val isSuccess: Boolean
    get() = errorCode == 0
}

// 正常响应情况调用方不需要 errcode, msg
data class ApiSuccessResponse<T>(val response: T?) : ApiResponse<T>(data = response)

class ApiEmptyResponse<T> : ApiResponse<T>()


// 业务逻辑异常
data class ApiBizError<T>(
  override val errorCode: Int?,
  override val errorMsg: String?
) : ApiResponse<T>(errorCode = errorCode, errorMsg = errorMsg)

// 其他技术异常：网络请求错误、反序列化错误等
data class ApiOtherError<T>(
  val throwable: Throwable
) : ApiResponse<T>(error = throwable)


/**
 * 返回 nullable 类型
 */
/*
// 调用方
lifecycleScope.launch {
  retrofit.create<UserApi>()
    .getUser(1)
    .getOrNull()
    ?.let { binding.nameLabel.text = it.name }
}
 */
fun <T> ApiResponse<T>.getOrNull(): T? = when (this) {
  is ApiSuccessResponse -> data
  is ApiBizError, is ApiOtherError, is ApiEmptyResponse -> null
  else -> null
}

/**
 * 抛异常
 */
// fun <T> ApiResponse<T>.getOrThrow(): T? = when (this) {
//   is ApiResponse.Success -> data
//   is ApiResponse.BizError -> throw BizException(retcode, retmsg)
//   is ApiResponse.OtherError -> throw throwable
// }


/*
getOrNull 常用于后接一个 ?.let 只处理成功情况；
如果失败的情况需要做些动作，则需用 guardSuccess
Nothing 是一个 空类型

// 调用方
lifecycleScope.launch {
  val user: User = retrofit.create<UserApi>()
    .getUser(1)
    .guardSuccess {
      pageState.value = PageState.Error
      return@launch
    }
  // ...
  // 拿到非 null 的 User 继续后面的业务逻辑
}
*/
inline fun <T> ApiResponse<T>.guardSuccess(block: () -> Nothing): T? {
  if (this !is ApiSuccessResponse) {
    block()
  }
  return this.data
}
