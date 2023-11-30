package com.keyboardhero.qr.di

import com.keyboardhero.qr.core.router.Router
import com.keyboardhero.qr.core.router.RouterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
abstract class RouterModule {
    @Binds
    @ActivityScoped
    abstract fun providerRouter(router: RouterImpl): Router
}