package com.nkle.poet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = Firebase.firestore
        val userInfo = intent.getSerializableExtra("user_data") as HashMap<String, Any>
        val loadingDialog = LoadingDialog(this)
        var users = mutableListOf<TrendingCard>()
        var posts = mutableListOf<Post>()
        var mainTrendingRecyclerView : RecyclerView = findViewById(R.id.rvTrendingCards);
        var mainTrendingCardAdapter = TrendingCardAdapter(users);
        val postCards : ViewPager2 = findViewById(R.id.post_cards)
        val mainPostSwipableAdapter = PostCardAdapter(posts)
        postCards.adapter = mainPostSwipableAdapter
        mainTrendingRecyclerView.adapter = mainTrendingCardAdapter;
        mainTrendingRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        loadingDialog.startLoading()
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser


        db.collection("users")
                .whereNotEqualTo("UID" , user?.uid)
                .get()
                .addOnSuccessListener {
                    Log.i("*******************" , userInfo["UID"].toString())

//                    it.document.da
//                    Toast.makeText(this, it.documents[0]["name"].toString(), Toast.LENGTH_SHORT).show()
                    for (doc in it.documents) {

                        Log.i("*******************" ,doc["name"].toString())

                        users.add(TrendingCard(doc.data!!["img_url"].toString() , doc.data!!["name"] as String,doc.id.toString(),
                                doc.data!!["likes"] as ArrayList<*>,  doc.data!!["poems"].toString().toInt() ,doc.data!!["UID"].toString(),
                                doc.data!!["password"].toString()))
                    }
//                    Toast.makeText(this, users[0].toString(), Toast.LENGTH_SHORT).show()

                    mainTrendingCardAdapter.notifyDataSetChanged()
                    db.collection("poems")
                            .get()
                            .addOnSuccessListener {
                                    for (doc in it.documents) {
                                        posts.add(Post(
                                                doc["name"].toString(),
                                                doc["title"].toString(),
                                                doc["body"].toString(),
                                                doc.id,
                                                userInfo["likes"] as ArrayList<Unit>,
                                                doc["like_count"].toString(),
                                            doc["img_url"].toString(),
                                               userInfo["user_id"].toString()
                                        ))
                                    }
                                mainPostSwipableAdapter.notifyDataSetChanged()
                                loadingDialog.isDismiss()
                            }.addOnFailureListener {
//                                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
                            }
//                    Toast.makeText(this, "successfully added", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "something went wrong wit your internet", Toast.LENGTH_SHORT).show()
                }
        //adding post_cards with swipable view demo data

        findViewById<ImageView>(R.id.feed_add_post)
            .setOnClickListener {
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
                Toast.makeText(this, user["poems"].toString(), Toast.LENGTH_SHORT).show()

//
                intent.putExtra("user_data", user)
                startActivity(intent)
            }
        findViewById<ImageView>(R.id.moon_btn)
                .setOnClickListener {
                    val intent = Intent(this , Profile::class.java)
//                    db.collection("poems")
//                            .whereEqualTo("user_id", "nnZWClJEVuWhAWExJFOmgBdyohL2")
//                            .get()
//                            .addOnSuccessListener {
//                                var ls = ArrayList<String>()
//                                for (doc in it.documents) {
//
//                                    ls.add(doc["title"].toString() + "," + doc.id.toString() + "," + doc["name"] + "," + doc["body"] + "," +
//                                            doc.id + "," + userInfo.contains(doc.id) + "," + doc["like_count"])
//                                }
////                                Toast.makeText(this,userInfo["img_url"].toString(), Toast.LENGTH_SHORT).show()
//                                val user = hashMapOf(
//                                    "name" to userInfo["name"],
//                                    "img_url" to userInfo["img_url"],
//                                    "likes" to userInfo["likes"],
//                                    "poems" to userInfo["poems"],
//                                    "user_id" to userInfo["user_id"],
//                                    "password" to userInfo["password"],
//                                    "uuid" to userInfo["uuid"],
//                                    "like_poems" to userInfo["likes"]
//                                )
//                                Log.i("this is me bro" , user.toString())
//
////
////                                Toast.makeText(this, user["name"].toString(), Toast.LENGTH_SHORT).show()
////                                intent.putExtra("img_url", userInfo["img_url"].toString())
                                intent.putExtra("user_data",userInfo)
//                                intent.putExtra(  "poems" , ls)
                                startActivity(intent)
//                            }.addOnFailureListener {
//                                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
//                            }
                }
    }
}