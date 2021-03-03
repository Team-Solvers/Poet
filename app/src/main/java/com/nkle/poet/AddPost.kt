package com.nkle.poet

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nkle.poet.databinding.ActivityAddPostBinding

class AddPost : AppCompatActivity() {
    private lateinit var binding: ActivityAddPostBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        val root = binding.root
        auth = FirebaseAuth.getInstance()
        setContentView(root)
        db = Firebase.firestore
        var title = binding.title.text
        var content = binding.mainContent.text
        val userinfo = intent.getSerializableExtra("user_data")!! as HashMap<*, *>
        binding.logedIn.text = userinfo["name"].toString()
        if(userinfo["img_url"] != "1") {
            DownloadImageFromInternet(findViewById<ImageView>(R.id.profile_image)).execute(userinfo["img_url"].toString())
        }
        binding.addPost.setOnClickListener {
            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Can not me Empty", Toast.LENGTH_SHORT).show()
            } else {
                if(content.length < 10) {
                    Toast.makeText(this, "content l", Toast.LENGTH_SHORT).show()
                } else {

                    var poem = hashMapOf(
                            "title" to title.toString(),
                            "body" to content.toString(),
                            "like_count" to 0,
                            "name" to userinfo["name"].toString(),
                            "img_url" to userinfo["img_url"].toString(),
                            "user_id" to userinfo["user_id"].toString()
                    )




                    val isloading = LoadingDialog(this)
//                    Toast.makeText(this, userinfo["img_url"].toString(), Toast.LENGTH_SHORT).show()
                    isloading.startLoading()
                    val user = auth.currentUser
                    db.collection("poems")
                            .add(poem)
                            .addOnSuccessListener {
                                isloading.isDismiss()
                                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                db.collection("users")
                                        .document(userinfo["user_id"].toString())
                                        .update("poems" , userinfo["poems"].toString().toInt() + 1)
                                        .addOnSuccessListener {
                                            val user = hashMapOf(
                                                    "name" to userinfo["name"],
                                                    "img_url" to userinfo["img_url"],
                                                    "likes" to userinfo["likes"],
                                                    "poems" to userinfo["poems"],
                                                    "user_id" to userinfo["user_id"],
                                                    "password" to userinfo["password"],
                                                    "uuid" to userinfo["uuid"],
                                            )
                                            intent.putExtra("user_data", userinfo)
                                            startActivity(intent)
                                        }
                                        .addOnFailureListener {
                                            Log.i("error man", it.toString())
                                            Toast.makeText(this, "Something went wrong please restart your App.", Toast.LENGTH_SHORT).show()
                                        }

                            }
                            .addOnFailureListener {
                                isloading.isDismiss()
                                Toast.makeText(this, "Please try again with a stable Connection.", Toast.LENGTH_SHORT).show()
                            }
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