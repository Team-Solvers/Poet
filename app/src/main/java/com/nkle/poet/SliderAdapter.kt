package com.nkle.poet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter

class SliderAdapter(context: Context): PagerAdapter() {
    var ctx: Context = context;


    lateinit var layoutInflater: LayoutInflater
    val Heading: Array<String> = arrayOf("Poetry", "SLEEP", "CODE")
    val slide: Array<String> = arrayOf(
        "Poetry is the spontaneous overflow of powerful feelings: it takes its origin from emotion recollected in tranquility.",
        "Poetry is an orphan of silence. The words never quite equal the experience behind them.",
        "The poet is a liar who always speaks the truth."
    );
    val slide_images: Array<Int> = arrayOf(R.drawable.first_slide, R.drawable.first_slide, R.drawable.third_slide)



    override fun getCount(): Int {
        return Heading.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        layoutInflater = this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.slide_layout,container,false)
        val slideImageView: ImageView = view.findViewById(R.id.china)
        val slideHeading: TextView = view.findViewById(R.id.slider_heading)
        val description: TextView = view.findViewById(R.id.description)

        slideImageView.setImageResource(slide_images[position])
        slideHeading.setText(Heading[position])
        description.setText(slide[position])

        container.addView(view)

        return view
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}