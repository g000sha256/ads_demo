package ru.g000sha256.ads_demo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.g000sha256.ads_demo.manager.AdsManager
import ru.g000sha256.schedulers.SchedulersHolder

class MainAdapter(
    private val adsManager: AdsManager,
    private val schedulersHolder: SchedulersHolder,
    private val useMock: Boolean
) : RecyclerView.Adapter<MainHolder>() {

    override fun getItemCount(): Int {
        return 100
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.main_item, parent, false)
        return MainHolder(adsManager, useMock, schedulersHolder, view)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {}

}