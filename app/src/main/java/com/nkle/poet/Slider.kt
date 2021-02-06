package com.nkle.poet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager

class Slider : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slider)
        val slideViewPager: ViewPager = findViewById<ViewPager>(R.id.slide_view_pager)
        val sliderAdapter: SliderAdapter = SliderAdapter(this)
        slideViewPager.adapter = sliderAdapter
    }
}