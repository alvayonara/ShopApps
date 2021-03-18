package com.alvayonara.shopapps.di

import com.alvayonara.shopapps.domain.usecase.ShopInteractor
import com.alvayonara.shopapps.domain.usecase.ShopUseCase
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {

    @Binds
    abstract fun provideMovieUseCase(shopInteractor: ShopInteractor): ShopUseCase
}