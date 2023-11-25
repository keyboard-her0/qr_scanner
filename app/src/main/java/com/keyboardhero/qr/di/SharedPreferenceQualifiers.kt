package com.keyboardhero.qr.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class SecurePreference

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class NormalPreference
