package com.wuc.kotlin_coroutines_net

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.wuc.base.base.BaseActivity
import com.wuc.base.util.*
import com.wuc.kotlin_coroutines_net.bean.WxArticleBean
import com.wuc.kotlin_coroutines_net.databinding.ActivityMainBinding
import com.wuc.kotlin_coroutines_net.login.LoginViewModel
import com.wuc.network.observerState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

class MainActivity : BaseActivity() {
  private val mViewModel by viewModels<LoginViewModel>()
  private lateinit var mBinding: ActivityMainBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mBinding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(mBinding.root)

    initData()
    initObserver()
  }

  private fun initObserver() {
    // 使用shareIn()将冷流转换成共享热流
    // scope: sharedFlow的启动作用域
    // started:启动策略
    //    SharingStarted.WhileSubscribed()  如果存在数据收集者,上游数据提供方保持活跃状态
    //    SharingStarted.Eagerly 立即启动数据提供方
    //    SharingStarted.Lazily存在数据收集者开始提供数据，并且永远保持活跃状态
    // replay 代表重放的数据个数
    //    replay 为0 代表不重放，也就是没有粘性
    //    replay 为1 代表重放最新的一个数据，后来的接收器能接受1个最新数据。
    //    replay 为2 代表重放最新的两个数据，后来的接收器能接受2个最新数据。
    mViewModel.uiState.shareIn(lifecycleScope, SharingStarted.Lazily, 0)
      .collectIn(this, Lifecycle.State.STARTED) {
        onSuccess = { result: List<WxArticleBean>? ->
          showNetErrorPic(false)
          mBinding.tvContent.text = result.toString()
        }
        onComplete = { Log.i("MainActivity", ": onComplete") }

        onFailed = { code, msg -> toast("errorCode: $code   errorMsg: $msg") }

        onError = { showNetErrorPic(true) }
      }

    mViewModel.uiState.collectIn(this, Lifecycle.State.STARTED) {
      onSuccess = { result: List<WxArticleBean>? ->
        showNetErrorPic(false)
        mBinding.tvContent.text = result.toString()
      }
      onComplete = { Log.i("MainActivity", ": onComplete") }

      onFailed = { code, msg -> toast("errorCode: $code   errorMsg: $msg") }

      onError = { showNetErrorPic(true) }
    }
  }

  private fun showNetErrorPic(isShowError: Boolean) {
    mBinding.tvContent.isGone = isShowError
    mBinding.ivContent.isVisible = isShowError
  }

  private fun initData() {
    mBinding.btnNet.setOnClickListener { requestNet() }

    mBinding.btnNetError.setOnClickListener {
      showNetErrorPic(false)
      requestNetError()
    }

    mBinding.btLogin.setOnClickListener {
      showNetErrorPic(false)
      login()
    }
  }

  private fun requestNet() {
    launchWithLoading(mViewModel::requestNet)
  }

  private fun requestNetError() {
    launchWithLoading(mViewModel::requestNetError)
  }

  /**
   * 链式调用，返回结果的处理都在一起，viewmodel中不需要创建一个livedata对象
   * 适用于不需要监听数据变化的场景
   * 屏幕旋转，Activity销毁重建，数据会消失
   */
  private fun login() {
    launchWithLoadingAndCollect(resquestBlock = {
      mViewModel.login2("FastJetpack", "FastJetpack")
    }) {
      onSuccess = {
        mBinding.tvContent.text = it.toString()
      }
      onFailed = { errorCode, errorMsg ->
        toast("errorCode: $errorCode   errorMsg: $errorMsg")
      }
    }
  }

  /**
   * 将Flow转变为LiveData
   */
  private fun loginAsLiveData() {
    val loginLiveData = launchFlow(resquestBlock = {
      mViewModel.login2("FastJetpack", "FastJetpack")
    }).asLiveData()

    loginLiveData.observerState(this) {
      onSuccess = {
        mBinding.tvContent.text = it.toString()
      }
      onFailed =
        { errorCode, errorMsg -> toast("errorCode: $errorCode   errorMsg: $errorMsg") }
    }
  }
}