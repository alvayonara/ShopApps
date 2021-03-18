package com.alvayonara.shopapps.ui.auth

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.alvayonara.shopapps.MyApplication
import com.alvayonara.shopapps.R
import com.alvayonara.shopapps.core.base.BaseActivity
import com.alvayonara.shopapps.core.data.source.remote.network.Result
import com.alvayonara.shopapps.core.ui.ViewModelFactory
import com.alvayonara.shopapps.core.utils.*
import com.alvayonara.shopapps.core.utils.ConstPreferences.PREF_IS_LOGIN
import com.alvayonara.shopapps.core.utils.ConstPreferences.PREF_TOKEN
import com.alvayonara.shopapps.databinding.ActivityRegisterBinding
import com.alvayonara.shopapps.ui.dashboard.DashboardActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    @Inject
    lateinit var factory: ViewModelFactory
    private val authViewModel: AuthViewModel by viewModels {
        factory
    }

    private lateinit var email: String
    private lateinit var password: String

    override val bindingInflater: (LayoutInflater) -> ActivityRegisterBinding
        get() = ActivityRegisterBinding::inflate

    override fun loadInjector() = (application as MyApplication).appComponent.inject(this)

    override fun setup() {
        setupFormValidation()
        setupView()
        subscribeViewModel()
    }

    @SuppressLint("CheckResult")
    override fun setupFormValidation() {
        val emailStream = RxTextView.textChanges(binding.edtEmail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe { showEmailExistAlert(it) }

        val passwordConfirmationStream = Observable.merge(
            RxTextView.textChanges(binding.edtPassword)
                .map { password ->
                    password.toString() != binding.edtConfirmationPassword.text.toString()
                },
            RxTextView.textChanges(binding.edtConfirmationPassword)
                .map { confirmPassword ->
                    confirmPassword.toString() != binding.edtPassword.text.toString()
                }
        )
        passwordConfirmationStream.subscribe { showPasswordConfirmationAlert(it) }

        val invalidFieldsStream = Observable.combineLatest(
            emailStream,
            passwordConfirmationStream,
            { emailMatchStreamInvalid: Boolean, emailEmptyStreamInvalid: Boolean ->
                !emailMatchStreamInvalid && !emailEmptyStreamInvalid
            }
        )

        invalidFieldsStream.subscribe { isValid ->
            binding.btnRegister.isEnabled = isValid
        }
    }

    override fun setupView() {
        binding.btnRegister.setOnClickListener {
            email = binding.edtEmail.text.toString()
            password = binding.edtPassword.text.toString()
            authViewModel.setRegister(email, password)
            hideKeyboard(this)
        }
    }

    private fun showEmailExistAlert(isNotValid: Boolean) {
        binding.edtEmail.error = if (isNotValid) getString(R.string.email_not_valid) else null
    }

    private fun showPasswordConfirmationAlert(isNotValid: Boolean) {
        binding.edtConfirmationPassword.error =
            if (isNotValid) getString(R.string.password_confirmation_not_same) else null
    }

    override fun subscribeViewModel() {
        authViewModel.postRegister.onLiveDataResult {
            when (it.status) {
                Result.Status.LOADING -> binding.progressBar.visible()
                Result.Status.SUCCESS -> {
                    binding.progressBar.gone()
                    authViewModel.setLogin(email, password)
                }
                Result.Status.ERROR -> {
                    binding.progressBar.gone()
                    if (it.errorCode == 422)
                        binding.layoutRegister.snack(getString(R.string.email_taken))
                    else
                        binding.layoutRegister.snack(getString(R.string.internet_error))
                }
            }
        }

        authViewModel.postLogin.onLiveDataResult {
            when (it.status) {
                Result.Status.LOADING -> binding.progressBar.visible()
                Result.Status.SUCCESS -> {
                    binding.progressBar.gone()
                    setDataPreferenceString(this, PREF_TOKEN, "Bearer ${it.body?.token.orEmpty()}")
                    setDataPreferenceBoolean(this, PREF_IS_LOGIN, true)
                    moveActivityClearTask<DashboardActivity>()
                }
                Result.Status.ERROR -> {
                    binding.progressBar.gone()
                    if (it.errorCode == 400)
                        binding.layoutRegister.snack(getString(R.string.email_password_wrong))
                    else
                        binding.layoutRegister.snack(getString(R.string.internet_error))
                }
            }
        }
    }
}