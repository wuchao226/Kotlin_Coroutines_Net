# Kotlin_Coroutines_Net
封装 Retrofit + 协程 + Flow，实现优雅快速的网络请求

>[使用 Kotlin Flow 优化你的网络请求框架，减少模板代码](https://juejin.cn/post/7022823222928211975)

```
class LoginRepository : BaseRepository() {
  private val mService by lazy { service }

  suspend fun login(username: String, password: String): ApiResponse<User?> {
    return executeHttp { mService.login(username, password) }
  }

  suspend fun fetchWxArticleFromNet(): ApiResponse<List<WxArticleBean>> {
    return executeHttp {
      mService.getWxArticle()
    }
  }

  suspend fun fetchWxArticleError(): ApiResponse<List<WxArticleBean>> {
    return executeHttp {
      mService.getWxArticleError()
    }
  }
}
```
#### LoginViewModel 中使用
```
class LoginViewModel : BaseViewModel() {
   private val repository by lazy { LoginRepository() }
  // 使用StateFlow 替代livedata
//    val wxArticleLiveData = StateMutableLiveData<List<WxArticleBean>>()

  private val _uiState = MutableStateFlow<ApiResponse<List<WxArticleBean>>>(ApiResponse())
  val uiState: StateFlow<ApiResponse<List<WxArticleBean>>> = _uiState.asStateFlow()

  suspend fun requestNet() {
    _uiState.value = repository.fetchWxArticleFromNet()
  }

  suspend fun requestNetError() {
    _uiState.value = repository.fetchWxArticleError()
  }

  /**
   * 场景：不需要监听数据变化
   */
  suspend fun login2(username: String, password: String): ApiResponse<User?> {
    return repository.login(username, password)
  }

  val userLiveData = StateLiveData<User?>()

  fun login(username: String, password: String) {
    
    viewModelScope.launch {
      // 不需要 loading 的使用
      repository.login(username, password)
        .getOrNull()
        ?.let {
          Log.i("user", it.toString())
        }
        
      // getOrNull 常用于后接一个 ?.let 只处理成功情况；
      // 如果失败的情况需要做些动作，则需用 guardSuccess
      // Nothing 是一个 空类型
      val user: User? = repository.login(username, password)
        .guardSuccess {
          // 网络请求不是 ApiSuccessResponse 的业务逻辑处理
          // ...
          return@launch
        }
      // ...
      // 拿到非 null 的 User 继续后面的业务逻辑
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

    initData()
    initObserver()
  }
  private fun initObserver() {
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
```
