package com.nkle.poet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostCardAdapter (var posts : List<Post>) : RecyclerView.Adapter<PostCardAdapter.PostCardViewHolder>(){

    inner class PostCardViewHolder(view : View)  : RecyclerView.ViewHolder(view) {
        val postAuthor: TextView = view.findViewById(R.id.post_author)
        val postTitle: TextView = view.findViewById(R.id.post_title)
        val postContent : TextView = view.findViewById(R.id.post_content)
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
            postContent.setText(R.string.post_content);
        }
    }
}