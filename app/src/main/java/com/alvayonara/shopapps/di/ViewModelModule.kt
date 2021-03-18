package com.alvayonara.shopapps.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alvayonara.shopapps.core.ui.ViewModelFactory
import com.alvayonara.shopapps.di.ViewModelKey
import com.alvayonara.shopapps.ui.auth.AuthViewModel
import com.alvayonara.shopapps.ui.dashboard.DashboardViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(viewModel: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindDashboardViewModel(viewModel: DashboardViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}