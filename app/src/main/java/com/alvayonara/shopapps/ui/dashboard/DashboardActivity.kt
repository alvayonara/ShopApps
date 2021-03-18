package com.alvayonara.shopapps.ui.dashboard

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvayonara.shopapps.MyApplication
import com.alvayonara.shopapps.core.base.BaseActivity
import com.alvayonara.shopapps.core.data.source.remote.network.Result.Status.*
import com.alvayonara.shopapps.core.ui.ItemAdapter
import com.alvayonara.shopapps.core.ui.ViewModelFactory
import com.alvayonara.shopapps.core.utils.*
import com.alvayonara.shopapps.core.utils.ConstPreferences.PREF_IS_LOGIN
import com.alvayonara.shopapps.core.utils.ConstPreferences.PREF_TOKEN
import com.alvayonara.shopapps.core.utils.ConstUiState.UI_STATE_ERROR
import com.alvayonara.shopapps.core.utils.ConstUiState.UI_STATE_LOADING
import com.alvayonara.shopapps.core.utils.ConstUiState.UI_STATE_LOGIN_FIRST
import com.alvayonara.shopapps.core.utils.ConstUiState.UI_STATE_NOT_FOUND
import com.alvayonara.shopapps.core.utils.ConstUiState.UI_STATE_SUCCESS
import com.alvayonara.shopapps.databinding.ActivityDashboardBinding
import com.alvayonara.shopapps.ui.auth.LoginActivity
import com.alvayonara.shopapps.ui.auth.RegisterActivity
import com.alvayonara.shopapps.ui.detail.DetailActivity
import com.alvayonara.shopapps.ui.detail.DetailActivity.Companion.EXTRA_ITEM_DETAIL
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class DashboardActivity : BaseActivity<ActivityDashboardBinding>() {

    @Inject
    lateinit var factory: ViewModelFactory
    private val dashboardViewModel: DashboardViewModel by viewModels {
        factory
    }

    private lateinit var itemAdapter: ItemAdapter

    private var token: String = ""
    private var isLogin: Boolean = false
    private var search: String = ""

    override val bindingInflater: (LayoutInflater) -> ActivityDashboardBinding
        get() = ActivityDashboardBinding::inflate

    override fun loadInjector() = (application as MyApplication).appComponent.inject(this)

    override fun setup() {
        setupView()
        setupRecyclerView()
        getListItem()
        subscribeViewModel()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setupView() {
        token = getDataPreferenceString(this, PREF_TOKEN).orEmpty()
        isLogin = getDataPreferenceBoolean(this, PREF_IS_LOGIN)

        dashboardViewModel.setToken(token)

        if (isLogin) {
            binding.fabAddItem.visible()
        } else {
            binding.fabAddItem.gone()
        }

        binding.btnLogout.setOnClickListener {
            if (isLogin) {
                setDataPreferenceBoolean(this, PREF_IS_LOGIN, false)
                moveActivityClearTask<DashboardActivity>()
            } else {
                moveActivity<LoginActivity>()
            }
        }

        binding.fabAddItem.setOnClickListener {
            startForResult.launch(
                Intent(this, DetailActivity::class.java)
            )
        }

        binding.viewLoginFirst.btnLogin.setOnClickListener {
            moveActivity<LoginActivity>()
        }

        binding.viewLoginFirst.btnRegister.setOnClickListener {
            moveActivity<RegisterActivity>()
        }

        binding.viewError.btnRetry.setOnClickListener {
            if (search != "") {
                dashboardViewModel.setSelectedSearch(search)
            } else {
                getListItem()
            }
        }

        binding.edtSearch.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || keyEvent.action == KeyEvent.ACTION_DOWN
                || keyEvent.action == KeyEvent.KEYCODE_ENTER
            ) {
                hideKeyboard(this)
                return@OnEditorActionListener true
            }
            false
        })

        binding.edtSearch.setOnTouchListener { _, _ ->
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            false
        }

        binding.btnClose.setOnClickListener {
            binding.edtSearch.setText("")
            binding.edtSearch.clearFocus()
            hideKeyboard(this)
            getListItem()
        }

        val searchStream = object : EditTextStream() {
            override fun onChanged(text: String) {
                search = text
                if (text.trim { it <= ' ' }.isEmpty()) {
                    binding.btnClose.gone()
                    getListItem()
                } else {
                    binding.btnClose.visible()
                    lifecycleScope.launch {
                        dashboardViewModel.setSelectedSearch(text)
                    }
                }
            }
        }

        binding.edtSearch.addTextChangedListener(searchStream)
    }

    override fun setupRecyclerView() {
        itemAdapter = ItemAdapter().apply {
            onEditClick = { data ->
                startForResult.launch(
                    Intent(this@DashboardActivity, DetailActivity::class.java).apply {
                        putExtra(EXTRA_ITEM_DETAIL, data)
                    }
                )
            }

            onDeleteClick = { data ->
                dashboardViewModel.setItemCode(data)
            }
        }

        with(binding.rvSearch) {
            layoutManager = LinearLayoutManager(this@DashboardActivity)
            setHasFixedSize(true)
            adapter = itemAdapter
        }
    }

    private fun getListItem() {
        dashboardViewModel.getListItem().onLiveDataResult {
            binding.viewFlipperDashboard.displayedChild = when (it.status) {
                LOADING -> UI_STATE_LOADING
                SUCCESS -> {
                    val data = it.body?.map { data ->
                        DataMapper.mapItemResponsesToDomain(data)
                    }
                    itemAdapter.submitList(data)
                    UI_STATE_SUCCESS
                }
                ERROR -> if (it.errorCode == 400 || it.errorCode == 401) UI_STATE_LOGIN_FIRST else UI_STATE_ERROR
            }
        }
    }

    override fun subscribeViewModel() {
        dashboardViewModel.searchBarang.onLiveDataResult {
            binding.viewFlipperDashboard.displayedChild = when (it.status) {
                LOADING -> UI_STATE_LOADING
                SUCCESS -> {
                    if (it.body?.message == "") {
                        val data = it.body.let { data ->
                            DataMapper.mapItemResponsesToDomain(data)
                        }
                        setLog(data.toString())
                        setLog(listOf(data).toString())
                        itemAdapter.submitList(listOf(data))
                        UI_STATE_SUCCESS
                    } else {
                        UI_STATE_NOT_FOUND
                    }
                }
                ERROR -> if (it.errorCode == 400 || it.errorCode == 401) UI_STATE_LOGIN_FIRST else UI_STATE_ERROR
            }
        }

        dashboardViewModel.postDeleteItem.onLiveDataResult {
            binding.viewFlipperDashboard.displayedChild = when (it.status) {
                LOADING -> UI_STATE_LOADING
                SUCCESS -> {
                    onResume()
                    UI_STATE_SUCCESS
                }
                ERROR -> if (it.errorCode == 400 || it.errorCode == 401) UI_STATE_LOGIN_FIRST else UI_STATE_ERROR
            }
        }
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                onResume()
            }
        }

    override fun onResume() {
        super.onResume()
        getListItem()
    }
}