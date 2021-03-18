package com.alvayonara.shopapps.core.di

import com.alvayonara.shopapps.core.data.source.ShopRepository
import com.alvayonara.shopapps.domain.repository.IShopRepository
import dagger.Binds
import dagger.Module

@Module(includes = [NetworkModule::class])
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(shopRepository: ShopRepository): IShopRepository
}