package com.keyboardhero.qr.shared.data

import com.keyboardhero.qr.shared.data.repositoryImpl.HistoryRepositoryImpl
import com.keyboardhero.qr.shared.domain.repository.HistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun providerAppPreference(preference: AppPreferenceImpl): AppPreference

    @Binds
    abstract fun providerHistoryRepository(repository: HistoryRepositoryImpl): HistoryRepository
}