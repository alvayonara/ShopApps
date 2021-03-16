package com.alvayonara.shopapps.ui.dashboard

import android.view.LayoutInflater
import com.alvayonara.shopapps.core.base.BaseActivity
import com.alvayonara.shopapps.databinding.ActivityDashboardBinding

class DashboardActivity : BaseActivity<ActivityDashboardBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityDashboardBinding
        get() = ActivityDashboardBinding::inflate

    override fun loadInjector() {
    }

    override fun setup() {
    }
}