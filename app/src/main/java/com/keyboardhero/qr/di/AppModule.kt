package com.keyboardhero.qr.di

import android.content.Context
import android.hardware.camera2.CameraManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import androidx.room.Room
import com.keyboardhero.qr.shared.data.db.AppDataBase
import com.keyboardhero.qr.shared.data.db.dao.HistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

/**
 * Defines all the classes that need to be provided in the scope of the app.
 *
 * Define here all objects that are shared throughout the app, like SharedPreferences, navigators or
 * others. If some of those objects are singletons, they should be annotated with `@Singleton`.
 */
@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideAppDataBase(@ApplicationContext context: Context): AppDataBase {
        return Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "qr_data"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun providerHistoryDao(appDataBase: AppDataBase): HistoryDao{
        return appDataBase.historyDao()
    }

    @Provides
    fun provideWifiManager(@ApplicationContext context: Context): WifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager

    @ApplicationScope
    @Singleton
    @Provides
    fun providesApplicationScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

    @Provides
    fun providerCameraManager(@ApplicationContext context: Context): CameraManager{
        return context.applicationContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
}
