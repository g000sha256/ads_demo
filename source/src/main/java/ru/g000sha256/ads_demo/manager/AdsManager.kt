package ru.g000sha256.ads_demo.manager

import android.content.Context
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.formats.UnifiedNativeAd
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleEmitter
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import ru.g000sha256.schedulers.SchedulersHolder

class AdsManager(private val context: Context, private val schedulersHolder: SchedulersHolder) {

    private val count = AtomicInteger(0)

    @Volatile
    private var isInitialized = false

    fun load(useMock: Boolean): Single<AdItem> {
        return Single
            .create<AdItem> { if (useMock) loadFromMock(it) else loadFromGoogle(it) }
            .subscribeOn(schedulersHolder.ioScheduler)
            .retryWhen { it.delay(1L, TimeUnit.SECONDS, schedulersHolder.computationScheduler) }
    }

    private fun createAdItem(): AdItem {
        val count = count.incrementAndGet()
        val thread = Thread.currentThread()
        return AdItem("Ad $count (${thread.name})")
    }

    private fun destroyUnifiedNativeAd(unifiedNativeAd: UnifiedNativeAd) {
        Completable
            .fromAction { unifiedNativeAd.destroy() }
            .subscribeOn(schedulersHolder.ioScheduler)
            .subscribe()
    }

    private fun loadFromMock(singleEmitter: SingleEmitter<AdItem>) {
        if (singleEmitter.isDisposed) return
        try {
            Thread.sleep(1000L)
        } catch (interruptedException: InterruptedException) {
            return
        }
        if (singleEmitter.isDisposed) return
        val adItem = createAdItem()
        singleEmitter.onSuccess(adItem)
    }

    private fun loadFromGoogle(singleEmitter: SingleEmitter<AdItem>) {
        if (singleEmitter.isDisposed) return
        if (!isInitialized) {
            isInitialized = true
            MobileAds.initialize(context)
        }
        val adListener = object : AdListener() {

            override fun onAdFailedToLoad(errorCode: Int) {
                if (singleEmitter.isDisposed) return
                val throwable = Throwable()
                singleEmitter.onError(throwable)
            }

        }
        val adRequest = AdRequest.Builder()
            .build()
        AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110")
            .forUnifiedNativeAd {
                destroyUnifiedNativeAd(it)
                if (singleEmitter.isDisposed) return@forUnifiedNativeAd
                val adItem = createAdItem()
                singleEmitter.onSuccess(adItem)
            }
            .withAdListener(adListener)
            .build()
            .loadAd(adRequest)
    }

}