# Kotlin_Coroutines_Net
封装 Retrofit+协程，实现优雅快速的网络请求

```
class LoginRepository : BaseRepository() {
  private val service by lazy { getService(UserApi::class.java) }

  suspend fun login(username: String, password: String): ApiResponse<User?> {
    return executeHttp { service.login(username, password) }
  }
}
```
#### LoginViewModel 中使用
```
class LoginViewModel : BaseViewModel() {
  private val reponse by lazy { LoginRepository() }

  val userLiveData = StateLiveData<User?>()

  fun login(username: String, password: String) {
    
    viewModelScope.launch {
      // 不需要 loading 的使用
      reponse.login(username, password)
        .getOrNull()
        ?.let {
          Log.i("user", it.toString())
        }
      // 需要 loading 的使用
      launchWithLoading(requestBlock = {
        reponse.login(username, password)
      }, resultCallback = {
        userLiveData.value = it
        it
          .getOrNull()
          ?.let { user ->
            Log.i("user", user.toString())
          }
      })
    }
  }
}
```
#### Activity 中使用
```
class MainActivity : BaseActivity() {
  private val mViewModel by viewModels<LoginViewModel>()
  private lateinit var mBinding: ActivityMainBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mBinding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(mBinding.root)

    mBinding.btnLogin.setOnClickListener {
      // 请求网络
      mViewModel.login("FastJetpack", "FastJetpack")
    }
    // 注册监听
    mViewModel.userLiveData.observerState(this) {
      onSuccess {
        Log.i("user", "livedata-->" + it.toString())
      }
    }
  }
}
```
### 如果需要单独处理每一个回调
```
mViewModel.userLiveData.observeState(this) {
    onSuccess { data ->
        Log.i("wutao","网络请求的结果是：$data")
    }
    
    onEmpty{
        Log.i("wutao", "返回的数据是空，展示空布局")
    }
    
    onFailed {
        Log.i("wutao", "后台返回的errorCode: $it")
    }

    onException { e ->
        Log.i("wutao","这是非后台返回的异常回调")
    }

    onShowLoading {
         Log.i("wutao","自定义单个请求的Loading")
    }

    onComplete {
        Log.i("wutao","网络请求结束")
    }
}
```
