package ru.g000sha256.ads_demo

import android.app.Activity
import android.os.Bundle
import android.widget.CheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val checkBoxView = findViewById<CheckBox>(R.id.check_box_view)
        checkBoxView.setOnCheckedChangeListener { _, isChecked ->
            val mainApplication = application as MainApplication
            val adsManager = mainApplication.adsManager
            val schedulersHolder = mainApplication.schedulersHolder
            if (isChecked) {
                recyclerView.adapter = MainAdapter(adsManager, schedulersHolder, useMock = true)
            } else {
                recyclerView.adapter = MainAdapter(adsManager, schedulersHolder, useMock = false)
            }
        }
        checkBoxView.isChecked = true
    }

}