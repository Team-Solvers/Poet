package com.nkle.poet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrendingCardAdapter (var trendingCards : List<TrendingCard>) : RecyclerView.Adapter<TrendingCardAdapter.TrendingCardViewHolder>(){

    inner class TrendingCardViewHolder(view : View)  : RecyclerView.ViewHolder(view) {
        val cardImage: ImageView = view.findViewById(R.id.trending_card_image)
        val cardTitle : TextView = view.findViewById(R.id.trending_poet_name)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TrendingCardViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_trending_card, viewGroup, false)

        return TrendingCardViewHolder(view)
    }

    override fun getItemCount() = trendingCards.size

    override fun onBindViewHolder(viewHolder: TrendingCardViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.apply {
            cardImage.setBackgroundResource(R.drawable.trending_image2);
            cardTitle.text = trendingCards[position].poetName;
        }
    }
}