package com.nkle.poet

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nkle.poet.databinding.ActivityPostDetailBinding
import com.nkle.poet.databinding.ActivityUserProfileBinding

class UserProfile : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<MyPoemRecyclerAdapter.myPoemViewHolder>? = null

    private lateinit var binding: ActivityUserProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        var root = binding.root
        setContentView(root)
        db = Firebase.firestore
        layoutManager = LinearLayoutManager(this)
        var a = binding.pView
        a.layoutManager = layoutManager
        val userInfo = intent.getSerializableExtra("user_data")!! as HashMap<*, *>
//        Toast.makeText(this, userInfo["author"].toString(), Toast.LENGTH_SHORT).show()
        binding.likes.text = "Likes: " + (userInfo["like_count"] as ArrayList<*>).size
        binding.myPoems.text = 0.toString()
        binding.profileName.text = userInfo["author"].toString()
//
//            Toast.makeText(this, userInfo["img_url"].toString(), Toast.LENGTH_SHORT).show()
//
////            val xdata = intent.getStringArrayListExtra("poems")
//            val userinfo = intent.getSerializableExtra("user_data")!! as HashMap<*, *>
//
            val loading = LoadingDialog(this)
//            DownloadImageFromInternet(findViewById<ImageView>(R.id.profile)).execute(userinfo["img_url"].toString())
//
////            DownloadImageFromInternet(findViewById(R.id.profile)).execute(intent.getStringExtra(userinfo["img_url"].toString().trim()))
////            this is a demo
//
            loading.startLoading()
            db.collection("poems")
                .whereEqualTo("user_id", userInfo["uuid"])
                .get()
                .addOnSuccessListener {
//
                    var ls = mutableListOf<HashMap<*,*>>()
                    val user = hashMapOf(
                        "name" to userInfo["author"],
                        "img_url" to userInfo["img_url"],
                        "likes" to userInfo["likes"],
                        "poems" to userInfo["poems"],
                        "user_id" to userInfo["id"],
                        "password" to userInfo["password"],
                        "uuid" to userInfo["uuid"],
                    )
//                    Toast.makeText(this , it.documents.toString(), Toast.LENGTH_SHORT).show()
                    for (doc in it.documents) {

//                        Toast.makeText(this, doc["title"].toString(), Toast.LENGTH_SHORT).show()
                        ls.add(hashMapOf(
                            "title" to doc["title"].toString(),
                            "id" to doc.id,
                            "user_id" to doc["user_id"],
                            "content" to doc["body"],
                            "img_url" to doc["img_url"],
                            "name" to userInfo["name"],
                            "like_count" to userInfo["like_count"],
                            "likes" to user["like_count"],
                            "poems" to user["poems"],
                            "password" to user["password"],
                            "uuid" to user["uuid"],
                        ))

                    }

                    binding.myPoems.text = "Poems: " + ls.size.toString()

//
                    adapter = MyPoemRecyclerAdapter(ls)
                    a.adapter = adapter
//                    adapter.notifyDataSetChanged()
                    loading.isDismiss()
//                    Toast.makeText(this, userInfo["img_url"].toString(), Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "couldn't load", Toast.LENGTH_SHORT).show()
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