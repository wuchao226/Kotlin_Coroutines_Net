package com.wuc.kotlin_coroutines_net

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.wuc.base.base.BaseActivity
import com.wuc.kotlin_coroutines_net.databinding.ActivityMainBinding
import com.wuc.kotlin_coroutines_net.login.LoginViewModel

class MainActivity : BaseActivity() {
  private val mViewModel by viewModels<LoginViewModel>()
  private lateinit var mBinding: ActivityMainBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mBinding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(mBinding.root)

    mBinding.btnLogin.setOnClickListener {
      mViewModel.login("FastJetpack", "FastJetpack")
    }

    mViewModel.userLiveData.observerState(this) {
      onSuccess {
        Log.i("user", "livedata-->" + it.toString())
      }
    }
  }
}