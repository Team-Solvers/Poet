package com.nkle.poet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nkle.poet.databinding.ActivityAddPostBinding

class AddPost : AppCompatActivity() {
    private lateinit var binding: ActivityAddPostBinding
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
        db = Firebase.firestore
        var title = binding.title.text
        var content = binding.mainContent.text
        val userinfo = intent.getSerializableExtra("user_data")!! as HashMap<*, *>
        binding.logedIn.text = userinfo["name"].toString()
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
                    Toast.makeText(this, userinfo["img_url"].toString(), Toast.LENGTH_SHORT).show()
                    isloading.startLoading()
                    db.collection("poems")
                            .add(poem)
                            .addOnSuccessListener {
                                isloading.isDismiss()
                                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
//                                val intent = Intent(this, MainActivity::class.java)
//                                startActivity(intent)

                            }
                            .addOnFailureListener {
                                isloading.isDismiss()
                                Toast.makeText(this, "Please try again with a stable Cinnection", Toast.LENGTH_SHORT).show()
                            }
                }
            }

        }
    }
}