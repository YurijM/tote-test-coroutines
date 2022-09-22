package com.example.tote_test

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tote_test.firebase.FirebaseRepository
import com.example.tote_test.ui.main.MainActivity
import com.example.tote_test.utils.AppPreferences
import com.example.tote_test.utils.GAMBLER
import com.example.tote_test.utils.REPOSITORY
import com.example.tote_test.utils.toLog
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_main)

        AppPreferences.getPreferences(this)

        /*REPOSITORY = FirebaseRepository()
        REPOSITORY.initDB()*/

        /*lifecycleScope.launch {
            GAMBLER = REPOSITORY.getGambler()
            toLog("GAMBLER: $GAMBLER")
        }*/

        /*val intent = Intent(this@SplashActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)*/

        /*lifecycleScope.launch {
            GAMBLER = REPOSITORY.getGambler()

            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            //val args = MainActivityArgs(isSignedIn)
            //intent.putExtras(args.toBundle())
            startActivity(intent)
        }*/
    }
}