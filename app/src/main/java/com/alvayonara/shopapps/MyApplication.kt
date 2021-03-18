package com.alvayonara.shopapps

import android.app.Application
import com.alvayonara.shopapps.core.di.CoreComponent
import com.alvayonara.shopapps.core.di.DaggerCoreComponent
import com.alvayonara.shopapps.di.AppComponent
import com.alvayonara.shopapps.di.DaggerAppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import timber.log.Timber.DebugTree


@ExperimentalCoroutinesApi
open class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    private val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.factory().create(applicationContext)
    }

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(coreComponent)
    }
}