package com.nkle.poet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewpager.widget.ViewPager
import com.nkle.poet.databinding.ActivityProfileBinding
import com.nkle.poet.databinding.ActivitySliderBinding

class Slider : AppCompatActivity() {
    private lateinit var binding: ActivitySliderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySliderBinding.inflate(layoutInflater)
        var root = binding.root
        setContentView(root)
        val slideViewPager: ViewPager = binding.slideViewPager
        val sliderAdapter: SliderAdapter = SliderAdapter(this)
        slideViewPager.adapter = sliderAdapter

        binding.skipBtn.setOnClickListener {
            startActivity(Intent(this@Slider, Login::class.java))
            finish()
        }

    }
}