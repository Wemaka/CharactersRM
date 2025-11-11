package com.wemaka.charactersrm

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant


@HiltAndroidApp
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        plant(DebugTree())
    }
}