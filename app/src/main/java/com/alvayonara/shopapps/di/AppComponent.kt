package com.alvayonara.shopapps.di

import com.alvayonara.shopapps.core.di.CoreComponent
import com.alvayonara.shopapps.ui.auth.LoginActivity
import com.alvayonara.shopapps.ui.auth.RegisterActivity
import com.alvayonara.shopapps.ui.dashboard.DashboardActivity
import com.alvayonara.shopapps.ui.detail.DetailActivity
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AppScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [AppModule::class, ViewModelModule::class]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): AppComponent
    }

    fun inject(activity: RegisterActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: DashboardActivity)
    fun inject(activity: DetailActivity)
}