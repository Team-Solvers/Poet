package com.nkle.poet

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class PostCardAdapter (var posts : List<Post>) : RecyclerView.Adapter<PostCardAdapter.PostCardViewHolder>(){

    inner class PostCardViewHolder(view : View)  : RecyclerView.ViewHolder(view) {
        val postAuthor: TextView = view.findViewById(R.id.post_author)
        val postTitle: TextView = view.findViewById(R.id.post_title)
        val postContent : TextView = view.findViewById(R.id.post_content)
        val profileImage: ImageView? = view.findViewById<ImageView>(R.id.card_profile_picture)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PostCardViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_posts, viewGroup, false)

        return PostCardViewHolder(view)
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(viewHolder: PostCardViewHolder, position: Int) {

//         Get element from your dataset at this position and replace the
//         contents of the view with that element
        viewHolder.apply {
            postAuthor.text = posts[position].postAuthor
            postTitle.text = posts[position].postTitle
//            postContent.setText(R.string.post_content);
            postContent.text = posts[position].postContent
            if(posts[position].img_url == "1") {
                profileImage?.setBackgroundResource(R.drawable.trending_image2);
            }

            else {
                DownloadImageFromInternet(viewHolder.profileImage as ImageView, viewHolder.profileImage.context)
                    .execute(posts[position].img_url.toString())

            }
        }
        viewHolder.itemView.setOnClickListener {
            val context= viewHolder.postTitle.context
            val intent = Intent( context, PostDetail::class.java)
            val poem = hashMapOf(
                "author" to posts[position].postAuthor.toString(),
                "title" to posts[position].postTitle.toString(),
                "id" to posts[position].id,
                "img_url" to posts[position].img_url.toString(),
                "content" to posts[position].postContent.toString(),
                    "like_poems" to posts[position].arr,
                    "like_count" to posts[position].likeCount,
                    "user_id" to posts[position].user_id
            )
            intent.putExtra("poem",poem)
            context.startActivity(intent);
        }
    }



    @SuppressLint("StaticFieldLeak")
    @Suppress("DEPRECATION")
    private inner class DownloadImageFromInternet(var imageView: ImageView, val Ctx: Context) : AsyncTask<String, Void, Bitmap?>() {
        init {
            Toast.makeText(Ctx, "Loading Posts...",     Toast.LENGTH_SHORT).show()
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