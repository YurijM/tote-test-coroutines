package com.example.tote_test.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tote_test.R
import com.example.tote_test.firebase.FirebaseRepository
import com.example.tote_test.ui.main.MainActivity
import com.example.tote_test.utils.AppPreferences
import com.example.tote_test.utils.GAMBLER
import com.example.tote_test.utils.REPOSITORY
import com.example.tote_test.utils.toLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashFragment : Fragment(R.layout.fragment_splash) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        REPOSITORY = FirebaseRepository()
        REPOSITORY.initDB()

        AppPreferences.setIsAuth(false)

        lifecycleScope.launch(Dispatchers.IO) {
            //GAMBLER = REPOSITORY.getGambler()
            toLog("SplashFragment -> GAMBLER: $GAMBLER")

            withContext(Dispatchers.Main) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }
    }
}