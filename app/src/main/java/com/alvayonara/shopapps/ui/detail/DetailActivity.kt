package com.alvayonara.shopapps.ui.detail

import android.app.Activity
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.alvayonara.shopapps.MyApplication
import com.alvayonara.shopapps.R
import com.alvayonara.shopapps.core.base.BaseActivity
import com.alvayonara.shopapps.core.data.source.remote.network.Result.Status.*
import com.alvayonara.shopapps.core.ui.ViewModelFactory
import com.alvayonara.shopapps.core.utils.ConstPreferences
import com.alvayonara.shopapps.core.utils.getDataPreferenceString
import com.alvayonara.shopapps.core.utils.gone
import com.alvayonara.shopapps.core.utils.visible
import com.alvayonara.shopapps.databinding.ActivityDetailBinding
import com.alvayonara.shopapps.domain.model.Item
import com.alvayonara.shopapps.ui.dashboard.DashboardViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DetailActivity : BaseActivity<ActivityDetailBinding>() {

    @Inject
    lateinit var factory: ViewModelFactory
    private val dashboardViewModel: DashboardViewModel by viewModels {
        factory
    }

    companion object {
        const val EXTRA_ITEM_DETAIL = "extra_item_detail"
    }

    private lateinit var item: Item
    private var isEdited: Boolean = false
    private var token: String = ""

    override val bindingInflater: (LayoutInflater) -> ActivityDetailBinding
        get() = ActivityDetailBinding::inflate

    override fun loadInjector() = (application as MyApplication).appComponent.inject(this)

    override fun setup() {
        token = getDataPreferenceString(this, ConstPreferences.PREF_TOKEN).orEmpty()
        dashboardViewModel.setToken(token)

        if (getBundle() != null) {
            item = getBundle()?.getParcelable(EXTRA_ITEM_DETAIL)!!
            initDetail(item)
            binding.tvItemHeader.text = getString(R.string.edit_item)
            isEdited = true
        } else {
            binding.tvItemHeader.text = getString(R.string.new_item)
            isEdited = false
        }

        setupView()
        setupFormValidation()
        subscribeViewModel()
    }

    private fun initDetail(item: Item) {
        item.let {
            binding.apply {
                edtNamaBarang.setText(it.itemName)
                edtKodeBarang.setText(it.itemCode)
                edtHargaBarang.setText(it.itemPrice)
                edtJumlahBarang.setText(it.itemStock)
                edtSatuanBarang.setText(it.itemUnit)
                edtStatusBarang.setText(it.itemStatus)

                // Kode barang cannot edited (when update)
                edtKodeBarang.isEnabled = false
            }
        }
    }

    override fun setupView() {
        binding.btnSave.setOnClickListener {
            dashboardViewModel.setSelectedItem(
                Item(
                    itemName = binding.edtNamaBarang.text.toString(),
                    itemCode = binding.edtKodeBarang.text.toString(),
                    itemPrice = binding.edtHargaBarang.text.toString(),
                    itemStock = binding.edtJumlahBarang.text.toString(),
                    itemUnit = binding.edtSatuanBarang.text.toString(),
                    itemStatus = binding.edtStatusBarang.text.toString(),
                    message = ""
                )
            )
        }

        binding.btnClose.setOnClickListener {
            onBackPressed()
        }
    }

    override fun subscribeViewModel() {
        if (isEdited) {
            dashboardViewModel.postUpdateItem.onLiveDataResult {
                when (it.status) {
                    LOADING -> binding.progressBar.visible()
                    SUCCESS -> {
                        binding.progressBar.gone()
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                    ERROR -> {
                        binding.progressBar.gone()
                        showToast(getString(R.string.internet_error))
                    }
                }
            }
        } else {
            dashboardViewModel.postAddItem.onLiveDataResult {
                when (it.status) {
                    LOADING -> binding.progressBar.visible()
                    SUCCESS -> {
                        binding.progressBar.gone()
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                    ERROR -> {
                        binding.progressBar.gone()
                        showToast(getString(R.string.internet_error))
                    }
                }
            }
        }
    }
}