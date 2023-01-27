package com.example.tote_test.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tote_test.R
import com.example.tote_test.firebase.FirebaseRepository
import com.example.tote_test.ui.main.MainActivity
import com.example.tote_test.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashFragment : Fragment(R.layout.fragment_splash) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            REPOSITORY = FirebaseRepository()
            REPOSITORY.initDB()

            AppPreferences.getPreferences(requireContext())
            //toLog("isAuth: ${AppPreferences.getIsAuth()}")

            GAMBLER = REPOSITORY.getGambler(CURRENT_ID)

            withContext(Dispatchers.Main) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }
    }
}