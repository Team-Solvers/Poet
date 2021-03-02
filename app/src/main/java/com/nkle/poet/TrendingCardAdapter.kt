package com.nkle.poet

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

@Suppress("DEPRECATION")
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
            cardTitle.text = trendingCards[position].poetName;

            if(trendingCards[position].imagePath == "1") {
                cardImage.setBackgroundResource(R.drawable.trending_image2);
            }

            else {
                DownloadImageFromInternet(viewHolder.cardImage as ImageView, viewHolder.cardImage.context)
                    .execute(trendingCards[position].imagePath.toString())

            }

            viewHolder.cardImage.setOnClickListener {
                val intent = Intent(viewHolder.cardImage.context, UserProfile::class.java)
                val poem = hashMapOf(
                    "author" to trendingCards[position].poetName,
                    "title" to trendingCards[position].poetName,
                    "id" to trendingCards[position].user_id,
                    "uuid" to trendingCards[position].id,
                    "img_url" to trendingCards[position].imagePath,
                    "like_count" to trendingCards[position].likes,
                        "password" to trendingCards[position].password
                )
                intent.putExtra("user_data" , poem)
                viewHolder.cardImage.context.startActivity(intent)
            }
        }

    }


    @SuppressLint("StaticFieldLeak")
    @Suppress("DEPRECATION")
    private inner class DownloadImageFromInternet(var imageView: ImageView , val Ctx: Context) : AsyncTask<String, Void, Bitmap?>() {
        init {
            Toast.makeText(Ctx, "Please wait, it may take a few minute...",     Toast.LENGTH_SHORT).show()
        }
        override fun doInBackground(vararg urls: String): Bitmap? {
            val imageURL = urls[0]
            var image: Bitmap? = null
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
            return image
        }
        override fun onPostExecute(result: Bitmap?) {
            imageView.setImageBitmap(result)
        }
    }




}