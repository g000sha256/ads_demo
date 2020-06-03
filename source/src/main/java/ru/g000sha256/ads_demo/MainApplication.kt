package ru.g000sha256.ads_demo

import android.app.Application
import ru.g000sha256.ads_demo.manager.AdsManager
import ru.g000sha256.scheduler.MainSchedulerFactoryImpl
import ru.g000sha256.schedulers.SchedulersHolder
import ru.g000sha256.schedulers.SchedulersImpl

class MainApplication : Application() {

    val adsManager by lazy { AdsManager(this, schedulersHolder) }
    val schedulersHolder by lazy { createSchedulersHolder() }

    private fun createSchedulersHolder(): SchedulersHolder {
        val mainSchedulerFactory = MainSchedulerFactoryImpl()
        return SchedulersImpl(mainSchedulerFactory)
    }

}