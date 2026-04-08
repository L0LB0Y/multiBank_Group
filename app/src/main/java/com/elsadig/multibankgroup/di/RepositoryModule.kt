package com.elsadig.multibankgroup.di

import com.elsadig.multibankgroup.data.repository.StockRepositoryImpl
import com.elsadig.multibankgroup.domain.repository.IStockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStockRepository(impl: StockRepositoryImpl): IStockRepository
}