package com.nkle.poet

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nkle.poet.databinding.ActivityPostDetailBinding

@Suppress("DEPRECATION")
class PostDetail : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailBinding
    private lateinit var db: FirebaseFirestore
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        var root = binding.root
        setContentView(root)
        db = Firebase.firestore
        //edited
        val poem = intent.getSerializableExtra("poem") as HashMap<String, Any>
        binding.authorName.text = "BY: ${poem["author"].toString()}"
        binding.postTitle.text = poem["title"].toString()
        binding.postContent.text = poem["content"].toString()
        Toast.makeText(this, poem["author"].toString(), Toast.LENGTH_SHORT).show()
//        db = Firebase.firestore
//
////        var a = mutableListOf("0")
        var likes = poem["like_poems"].toString().replace("[","").replace("]", "").split(",")
        var likeID = mutableListOf<String>()
        for (l in likes) {
            likeID.add(l.trim())
        }
        var isLiked = likeID.contains(poem["id"].toString().trim())
        if(isLiked) {
            binding.likePost.setImageResource(R.drawable.like)
        } else {
            binding.likePost.setImageResource(R.drawable.unlike)

        }
//        Toast.makeText(this,poem["img_url"].toString(), Toast.LENGTH_SHORT).show()
        if(poem["img_url"].toString() != "1") {
            DownloadImageFromInternet(findViewById<ImageView>(R.id.post_author_image)).execute(poem["img_url"].toString())
        }
//
//        Toast.makeText(this,(binding.likePost.drawable == resources.getDrawable(R.drawable.unlike)).toString()   , Toast.LENGTH_SHORT).show()
//
        binding.likeCount.text = poem["like_count"].toString()
//
        binding.likePost.setOnClickListener {

            if(isLiked) {

                binding.likePost.setBackgroundResource(R.drawable.unlike)
//                likeID.removeAt()
                db.collection("poems")
                        .document(poem["id"].toString())
                            .update("like_count", poem["like_count"].toString().toInt() - 1)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Successfull", Toast.LENGTH_SHORT).show()
                            likeID.remove(poem["id"].toString())
                            db.collection("users")
                                    .document( poem["user_id"].toString())
                                    .update("likes", likeID)
                                    .addOnSuccessListener {
                                        isLiked = false
                                        binding.likeCount.text = (poem["like_count"].toString().toInt().toInt() -1).toString()
//                                        poem["like_count"] =
                                        poem.put("like_count", poem.get("like_count").toString().toInt() - 1 )
                                        Toast.makeText(this, "all is done here!", Toast.LENGTH_SHORT).show()
                                    }.addOnFailureListener {
                                        binding.likePost.setBackgroundResource(R.drawable.like)
                                        Toast.makeText(this, "Come one man", Toast.LENGTH_SHORT).show()
                                    }
                        }

                        .addOnFailureListener {
                            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                        }

            } else {
                binding.likePost.setBackgroundResource(R.drawable.like)
                likeID.removeAt(0)

                db.collection("poems")
                        .document(poem["id"].toString())
                        .update("like_count", poem["like_count"].toString().toInt() + 1)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Successfull", Toast.LENGTH_SHORT).show()
                            likeID.add(poem["id"].toString())
                            db.collection("users")
                                    .document( poem["user_id"].toString())
                                    .update("likes", likeID)
                                    .addOnSuccessListener {
                                        binding.likeCount.text = (poem["like_count"].toString().toInt() + 1).toString()
                                        isLiked = true
                                        poem.put("like_count", poem.get("like_count").toString().toInt() - 1 )

                                        Toast.makeText(this, "all is done here!", Toast.LENGTH_SHORT).show()
                                    }.addOnFailureListener {
                                        binding.likePost.setBackgroundResource(R.drawable.unlike)

                                        Toast.makeText(this, "Come one man", Toast.LENGTH_SHORT).show()
                                    }
                        }

                        .addOnFailureListener {
                            binding.likePost.setBackgroundResource(R.drawable.unlike)

                            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                        }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Suppress("DEPRECATION")
    private inner class DownloadImageFromInternet(var imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
        init {
            Toast.makeText(applicationContext, "Please wait, it may take a few minute...",     Toast.LENGTH_SHORT).show()
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