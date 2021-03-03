package com.nkle.poet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nkle.poet.databinding.ActivityLoginBinding


class Login : AppCompatActivity() {
    private lateinit var  mGoogleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN  = 1
    private val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"
    private lateinit var db:  FirebaseFirestore
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        var root = binding.root
        setContentView(root)
        auth = FirebaseAuth.getInstance()
        db = Firebase.firestore
        val userRef = db.collection("users")
        //request created
        createRequest()
        // google
        binding.signInButton.setOnClickListener {
            signIn()
        }

        //register listener
        binding.registerBtn.setOnClickListener {
            startActivity(Intent(this@Login, Register::class.java ))
            finish()
        }

        //login button listener
        binding.loginBtn.setOnClickListener {
            val pass = binding.loginPassword.text.toString()
            val email = binding.loginEmail.text.toString()
            if(email == "" || pass == "") {
                Toast.makeText(this, "can not be empty", Toast.LENGTH_SHORT).show()
            } else {
                val loading = LoadingDialog(this)
                loading.startLoading()
                auth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                val user = auth.currentUser
                                db.collection("users")
                                        .whereEqualTo("UID" , user?.uid)
                                        .get()
                                        .addOnSuccessListener {

                                              val newUser = hashMapOf(
                                                    "UID" to it.documents[0].data!!["UID"],
                                                    "user_id" to it.documents[0].id,
                                                    "name" to it.documents[0].data!!["name"],
                                                    "img_url" to it.documents[0].data!!["img_url"],
//                                                    "phone_number" to it.documents[0].data!!["phone"],
                                                    "poems" to it.documents[0].data!!["poems"],
                                                    "likes" to it.documents[0].data!!["likes"] as ArrayList<*>,
                                                    "email" to it.documents[0].data!!["email"],
                                            )
                                            Log.i("_________________-", newUser["user_id"].toString())
//                                            }
                                            loading.isDismiss()
                                            val intent = Intent(this@Login,MainActivity::class.java )
                                            intent.putExtra("user_data", newUser)
                                            startActivity(intent)
                                            Toast.makeText(this, "Signed in successfully", Toast.LENGTH_SHORT).show()
                                            finish()
                                        }
                            } else {
                                loading.isDismiss()
                                // If sign in fails, display a message to the user.
                                Toast.makeText(baseContext, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show()

                            }

                            // ...
                        }

            }
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun createRequest() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
//                Toast.makeText(this, "got google user data", Toast.LENGTH_SHORT).show()
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign zzIn failed, update UI appropriately
                // ...
                Toast.makeText(this, "some error occurred please check your internet! ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val loading = LoadingDialog(this)
        val intent = Intent(this, MainActivity::class.java)

        loading.startLoading()
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    
                    val newUser = hashMapOf(
                            "UID" to user?.uid,
                            "user_id" to user?.uid,
                            "name" to user?.displayName,
                            "img_url" to user?.photoUrl.toString(),
                            "phone_number" to user?.phoneNumber,
                            "poems" to 0,
                            "likes" to mutableListOf<String>(),
                            "email" to user?.email.toString(),
                            "like_count" to 0
                    )

                    db.collection("users")
                            .whereEqualTo("user_id", user?.uid)
                            .get()
                            .addOnSuccessListener {
                                if(it.size() == 0) {
                                    db.collection("users")
                                            .add(newUser)
                                            .addOnSuccessListener {
                                                intent.putExtra("user_data", newUser)
                                                startActivity(intent)
                                                Toast.makeText(this, "Well this was successful", Toast.LENGTH_SHORT).show()
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(this, "there was an error here bro", Toast.LENGTH_SHORT).show()
                                            }
                                            loading.isDismiss()
                                            intent.putExtra("user_data", newUser)
                                            startActivity(intent)
                                } else {
                                    loading.isDismiss()
                                    intent.putExtra("user_data", newUser)
                                    startActivity(intent)
//                                    Toast.makeText(this, "yeahye", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Toast.makeText(this, "register", Toast.LENGTH_SHORT).show()
                            }

//                    db.collection("users")
//                            .add(newUser)
//                            .addOnSuccessListener { it ->
//                                Toast.makeText(this , ("data successfully added to the store"), Toast.LENGTH_SHORT).show()
//                                val intent = Intent(this,Profile::class.java)
//                                intent.putExtra("photo_url" , newUser["img_url"].toString())
//                                intent.putExtra("name" , newUser["name"].toString())
//                                startActivity(intent)
//                            }
//
//                            .addOnFailureListener{
//                                Toast.makeText(this, "something went wrong when storing gmail data", Toast.LENGTH_SHORT).show()
//
//                            }
//
//
//
//                    // Log.i("saved UUID  __________________________", newUser["UID"].toString())
//                    db.collection("poems")
//                            .whereEqualTo("user_id", newUser["UID"])
//                            .get()
//                            .addOnSuccessListener {
//                                if(it.documents.size == 0) {
//                                    Toast.makeText(this, "no poems yet", Toast.LENGTH_SHORT).show()
//                                } else {
//                                    val list: ArrayList<String> = arrayListOf()
//                                    for (doc in it.documents) {
//                                        list.add(
//                                            doc["title"].toString()
////                                            myPoems(
////                                                doc["user_id"].toString(),
////                                                doc["title"].toString(),
////                                                doc["body"].toString()
////                                            )
//                                        )
//                                        Toast.makeText(this, doc["title"].toString(), Toast.LENGTH_SHORT).show()
//                                    }
////                                    Log.i(
////                                        " asd+__________________",
////                                        it.documents[0]["title"].toString()
////                                    )
////
////                                    val args = Bundle()
//                                    val intent = Intent(this, Profile::class.java)
////                                    intent.putParcelableArrayListExtra("poems" ,list as java.util.ArrayList<out Parcelable>)
//                                    intent.putStringArrayListExtra("poems",list)
//                                    intent.putExtra("photo_url", newUser["img_url"])
//                                    startActivity(intent)
//                                    Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
//
//                                }
//                            }
////                    vval intent = Intent(this )
////                    if(mGoogleSignInClient!= null) {
////                        intent.putExtra("abcd" , mGoogleSignInClient.toString())
////
////                    }
////                    intent.putExtra()
//
//

//                    Toast.makeText(this, user?.displayName, Toast.LENGTH_SHORT).show()
                } else {
                    // If sign in fails, display a message to the user.
                    // ...
                    Toast.makeText(this, "this has in firebase auth failed!!!", Toast.LENGTH_SHORT).show()
                }

                // ...
            }
    }
//
//    override fun onStart() {
//        super.onStart()
//        val currentUser = auth.currentUser
//        if(currentUser != null) {
//            val intent = Intent(this@Login, Profile::class.java)
//            startActivity(intent)
//        }
//    }
}



