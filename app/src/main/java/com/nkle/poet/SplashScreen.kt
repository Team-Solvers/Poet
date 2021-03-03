package com.nkle.poet

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class SplashScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        var sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        var editor = sharedPref.edit();

        var isFirstTime = sharedPref.getBoolean("isFirstTime",true);
        var isLoggedOut = sharedPref.getBoolean("isLoggedOut",false);

        if(isFirstTime){
            editor.apply{
                putBoolean("isFirstTime",false);
                commit();
            }
        }

        auth = FirebaseAuth.getInstance()
        Log.i("checkFirst1", "-----$isFirstTime----- is the first time and -------$isLoggedOut------ is the logout check");

        var online = isOnline(this);
        if(!online){
            Toast.makeText(this,"No stable connection found",Toast.LENGTH_SHORT).show()
        }
        else {
            Handler(Looper.getMainLooper()).postDelayed({
                val currentUser = auth.currentUser;
                if (currentUser == null) {
                    if (isFirstTime) {
                        startActivity(Intent(this@SplashScreen, Slider::class.java))
                    } else {
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

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}