package ru.g000sha256.ads_demo

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.g000sha256.ads_demo.manager.AdsManager
import ru.g000sha256.schedulers.SchedulersHolder

class MainHolder(
    private val adsManager: AdsManager,
    private val useMock: Boolean,
    private val schedulersHolder: SchedulersHolder,
    view: View
) : RecyclerView.ViewHolder(view) {

    private val compositeDisposable = CompositeDisposable()
    private val textView = view as TextView

    init {
        val onAttachStateChangeListener = object : View.OnAttachStateChangeListener {

            override fun onViewAttachedToWindow(v: View) {
                load()
            }

            override fun onViewDetachedFromWindow(v: View) {
                compositeDisposable.clear()
            }

        }
        view.addOnAttachStateChangeListener(onAttachStateChangeListener)
    }

    private fun load() {
        textView.text = "Loading..."
        val disposable = adsManager
            .load(useMock)
            .observeOn(schedulersHolder.mainDeferredScheduler)
            .subscribe(
                { textView.text = it.title },
                {}
            )
        compositeDisposable.add(disposable)
    }

}