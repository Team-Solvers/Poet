package com.nkle.poet

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class SplashScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

//        auth = FirebaseAuth.getInstance()
//        Handler(Looper.getMainLooper()).postDelayed({
////            if (auth.currentUser == null) {
//                startActivity(Intent(this@SplashScreen, Slider::class.java))
//            finish()
////            } else {
////                startActivity(Intent(this@SplashScreen, Login::class.java))
////
////            }
//        }, 4000)
        var sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        var editor = sharedPref.edit();

        var isFirstTime = sharedPref.getBoolean("isFirstTime",true);
        var isLoggedOut = sharedPref.getBoolean("isLoggedOut",false);

        auth = FirebaseAuth.getInstance()
        Log.i("checkFirst1", "-----$isFirstTime----- is the first time and -------$isLoggedOut------ is the logout check");

        Handler(Looper.getMainLooper()).postDelayed({
            val currentUser = auth.currentUser;
            if (currentUser == null) {
                if(isFirstTime){
                    editor.apply{
                        putBoolean("isFirstTime",false);
                        commit();
                    }
                    startActivity(Intent(this@SplashScreen, Slider::class.java))
                }
                else{
                    //user not first time but has logged out
                    //change logged out on the log in function
                    startActivity(Intent(this@SplashScreen, Login::class.java))
                }
            } else {
                //get user data
                //go to user data
                startActivity(Intent(this@SplashScreen, Login::class.java))
            }
            //don't show when user returns
            finish();
        }, 4000)
    }
}