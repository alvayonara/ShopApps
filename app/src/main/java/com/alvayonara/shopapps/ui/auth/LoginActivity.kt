package com.alvayonara.shopapps.ui.auth

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.alvayonara.shopapps.MyApplication
import com.alvayonara.shopapps.R
import com.alvayonara.shopapps.core.base.BaseActivity
import com.alvayonara.shopapps.core.data.source.remote.network.Result.Status.*
import com.alvayonara.shopapps.core.ui.ViewModelFactory
import com.alvayonara.shopapps.core.utils.*
import com.alvayonara.shopapps.core.utils.ConstPreferences.PREF_IS_LOGIN
import com.alvayonara.shopapps.core.utils.ConstPreferences.PREF_TOKEN
import com.alvayonara.shopapps.databinding.ActivityLoginBinding
import com.alvayonara.shopapps.ui.dashboard.DashboardActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    @Inject
    lateinit var factory: ViewModelFactory
    private val authViewModel: AuthViewModel by viewModels {
        factory
    }

    override val bindingInflater: (LayoutInflater) -> ActivityLoginBinding
        get() = ActivityLoginBinding::inflate

    override fun loadInjector() = (application as MyApplication).appComponent.inject(this)

    override fun setup() {
        setupFormValidation()
        setupView()
        subscribeViewModel()
    }

    @SuppressLint("CheckResult")
    override fun setupFormValidation() {
        val emailValidStream = RxTextView.textChanges(binding.edtEmail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailValidStream.subscribe { showEmailNotValidAlert(it) }

        val emailEmptyStream = RxTextView.textChanges(binding.edtEmail)
            .skipInitialValue()
            .map { email ->
                TextUtils.isEmpty(email)
            }
        emailEmptyStream.subscribe { showEmailEmptyAlert(it) }

        val passwordEmptyStream = RxTextView.textChanges(binding.edtPassword)
            .skipInitialValue()
            .map { email ->
                TextUtils.isEmpty(email)
            }
        emailEmptyStream.subscribe { showPasswordEmptyAlert(it) }

        val invalidFieldsStream = Observable.combineLatest(
            emailValidStream,
            emailEmptyStream,
            passwordEmptyStream,
            { emailInvalidStream: Boolean, emailEmptyStreamNotEmpty: Boolean, passwordEmptyStreamNotEmpty: Boolean ->
                !emailInvalidStream && !emailEmptyStreamNotEmpty && !passwordEmptyStreamNotEmpty
            }
        )

        invalidFieldsStream.subscribe { isValid ->
            binding.btnLogin.isEnabled = isValid
        }
    }

    override fun setupView() {
        binding.btnLogin.setOnClickListener {
            authViewModel.setLogin(
                email = binding.edtEmail.text.toString(),
                password = binding.edtPassword.text.toString()
            )
            hideKeyboard(this)
        }

        binding.btnToRegister.setOnClickListener {
            moveActivity<RegisterActivity>()
        }
    }

    override fun subscribeViewModel() {
        authViewModel.postLogin.onLiveDataResult {
            when (it.status) {
                LOADING -> binding.progressBar.visible()
                SUCCESS -> {
                    binding.progressBar.gone()
                    setDataPreferenceString(this, PREF_TOKEN, "Bearer ${it.body?.token.orEmpty()}")
                    setDataPreferenceBoolean(this, PREF_IS_LOGIN, true)
                    moveActivityClearTask<DashboardActivity>()
                }
                ERROR -> {
                    binding.progressBar.gone()
                    setLog(it.errorCode.toString())
                    if (it.errorCode == 400)
                        binding.layoutLogin.snack(getString(R.string.email_password_wrong))
                    else
                        binding.layoutLogin.snack(getString(R.string.internet_error))
                }
            }
        }
    }

    private fun showEmailNotValidAlert(isNotValid: Boolean) {
        binding.edtEmail.error = if (isNotValid) getString(R.string.email_not_valid) else null
    }

    private fun showEmailEmptyAlert(isEmpty: Boolean) {
        binding.edtEmail.error =
            if (isEmpty) getString(R.string.email_empty) else null
    }

    private fun showPasswordEmptyAlert(isEmpty: Boolean) {
        binding.edtPassword.error =
            if (isEmpty) getString(R.string.password_empty) else null
    }
}