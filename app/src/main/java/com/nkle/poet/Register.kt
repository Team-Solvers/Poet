package com.nkle.poet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nkle.poet.databinding.ActivityRegisterBinding
import java.util.*

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var db:FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
        db = Firebase.firestore
        auth = FirebaseAuth.getInstance()

        val f_name = binding.firstName
        val l_name = binding.lastName
        val email = binding.registerEmail
        val password = binding.registerPassword
        val confirmPassword = binding.registerConfirmPassword
        val loading = LoadingDialog(this)
        binding.registerBtn.setOnClickListener {
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
            if(f_name.text.toString() == "" ||   l_name.text.toString() == ""
                    || email.text.toString() == "" || password.text.toString() == "" ||
                            confirmPassword.text.toString() == ""
            )
            {
                Toast.makeText(this, "can not be empty", Toast.LENGTH_SHORT).show()
            }
            else
            {

                if(password.text.toString() != confirmPassword.text.toString()) {
                    Toast.makeText(this, "password missmatch", Toast.LENGTH_SHORT).show()
                }
                else {
                    if(password.text.toString().length < 6) {
                        Toast.makeText(this, "Password too short must be atleast 6 characters!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        val loadding = LoadingDialog(this)
                        loadding.startLoading()
                        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information

                                        val user = auth.currentUser
                                        val userInfo = hashMapOf(
                                                "name" to "${f_name.text.toString()} ${l_name.text.toString()}",
                                                "img_url" to "1",
                                                "poems" to 0,
                                                "email" to email.text.toString(),
                                                "user_id" to user?.uid.toString(),
                                                "UID" to user?.uid.toString(),
                                                "password" to password.text.toString(),
                                                "likes" to arrayListOf<String>(),
                                                "poems" to 0,
                                                "email" to user?.email
                                                )

                                        Toast.makeText(this,userInfo.toString(), Toast.LENGTH_SHORT).show()
                                        db.collection("users")
                                                .add(userInfo)
                                                .addOnSuccessListener {
                                                    val intent = Intent(this, Profile::class.java)
                                                    intent.putExtra("user_data", userInfo)
                                                    intent.putExtra("from" , "register")
                                                    startActivity(intent)
                                                    loadding.isDismiss()
    //                                                Toast.makeText(this, "registered Successfully", Toast.LENGTH_SHORT).show()
                                                }
                                                .addOnFailureListener {
                                                    Toast.makeText(this, "unsuccess full storing", Toast.LENGTH_SHORT).show()
                                                    val intent = Intent(this, Profile::class.java)
                                                    intent.putExtra("from" , "register")
                                                    intent.putExtra("user_data" , userInfo)
                                                    loadding.isDismiss()

                                                    startActivity(intent)
                                                    finish()
                                                }
                                    } else {
                                        loadding.isDismiss()

                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(baseContext, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show()
                                    }

                                    // ...
                                }
                        }



//                    db.collection("users")
//                            .add(userInfo)
//
//                            .addOnSuccessListener {
//                                loading.isDismiss()
//                                val intent = Intent(this, Profile::class.java)
//                                intent.putExtra("user_data", userInfo)
//                                intent.putExtra("from" , "register")
//                                startActivity(intent)
//                                Toast.makeText(this, "registered Successfully", Toast.LENGTH_SHORT).show()
//                            }
//                            .addOnFailureListener {
//                                loading.isDismiss()
//                                Toast.makeText(this, "registered failed", Toast.LENGTH_SHORT).show()
//                            }

                }
            }

        }



    }
}