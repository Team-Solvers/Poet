@file:Suppress("DEPRECATION")

package com.nkle.poet

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nkle.poet.databinding.ActivityProfileBinding


class Profile : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<MyPoemRecyclerAdapter.myPoemViewHolder>? = null
    lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityProfileBinding

    private lateinit var db: FirebaseFirestore
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        db = Firebase.firestore
        auth = FirebaseAuth.getInstance()
        setContentView(view)
        layoutManager = LinearLayoutManager(this)
        var a = binding.rView
        a.layoutManager = layoutManager
        val userInfo = intent.getSerializableExtra("user_data")!! as HashMap<String, Any>
        binding.likes.text = "Likes: " + (userInfo["likes"] as ArrayList<String>).size
        binding.myPoems.text = "Poems: " + userInfo["poems"].toString()
        binding.profileName.text = userInfo["name"].toString()
        if (intent.getStringExtra("from") == "register") {

            val arr = mutableListOf<HashMap<*, *>>(
                    hashMapOf(
                            "posts" to "No current users"
                    )
            )
            adapter = MyPoemRecyclerAdapter(arr)
            a.adapter = adapter
            val userinfo = intent.getSerializableExtra("user_data") as HashMap<String, Unit>
            binding.profile.setImageResource(R.drawable.default_profile)

        }
        else {
//            Toast.makeText(this, userInfo["img_url"].toString(), Toast.LENGTH_SHORT).show()

//            val data = intent.getStringArrayListExtra("poems")
            val userinfo = intent.getSerializableExtra("user_data")!! as HashMap<*, *>

            val loading = LoadingDialog(this)
            if(userInfo["img_url"].toString() == "1") {
                binding.profile.setImageResource(R.drawable.default_profile)
            } else {
                DownloadImageFromInternet(findViewById<ImageView>(R.id.profile)).execute(userinfo["img_url"].toString())
            }

//            DownloadImageFromInternet(findViewById(R.id.profile)).execute(intent.getStringExtra(userinfo["img_url"].toString().trim()))
//            this is a demo


            val user = auth.currentUser
            loading.startLoading()
            db.collection("poems")
                .whereEqualTo("user_id", userInfo["user_id"])
                .get()
                .addOnSuccessListener {

                    var ls = mutableListOf<HashMap<*, *>>()
                    val user = hashMapOf(
                            "name" to userInfo["name"],
                            "img_url" to userInfo["img_url"],
                            "likes" to userInfo["likes"],
                            "poems" to userInfo["poems"],
                            "user_id" to userInfo["user_id"],
                            "password" to userInfo["password"],
                            "uuid" to userInfo["uuid"],
                    )
                    for (doc in it.documents) {
                        ls.add(hashMapOf(
                                "title" to doc["title"].toString(),
                                "id" to doc.id,
                                "user_id" to doc["user_id"],
                                "content" to doc["body"],
                                "img_url" to doc["img_url"],
                                "name" to doc["name"],
                                "like_count" to doc["like_count"],
                                "likes" to userInfo["likes"],
                                "poems" to userInfo["poems"],
                                "password" to userInfo["password"],
                                "uuid" to userInfo["uuid"],
                        ))
                    }

                    adapter = MyPoemRecyclerAdapter(ls)
                    a.adapter = adapter
                    loading.isDismiss()
                }.addOnFailureListener {
                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
                }
        }

        binding.registerLogout.setOnClickListener {
            auth.signOut()
            var sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
            var editor = sharedPref.edit();

            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finishAffinity()
        }
        binding.registerAddPost.setOnClickListener {
            val intent = Intent(this, AddPost::class.java)
            val user = hashMapOf(
                    "name" to userInfo["name"],
                    "img_url" to userInfo["img_url"],
                    "likes" to userInfo["likes"],
                    "poems" to userInfo["poems"],
                    "user_id" to userInfo["user_id"],
                    "password" to userInfo["password"],
                    "uuid" to userInfo["uuid"],
            )

            intent.putExtra("user_data", user)
            startActivity(intent)
        }

        binding.sunBtn.setOnClickListener {
            finish();
        }
     }



    @SuppressLint("StaticFieldLeak")
    @Suppress("DEPRECATION")
    private inner class DownloadImageFromInternet(var imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
        init {
            Toast.makeText(applicationContext, "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show()
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