package com.example.tote_test.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tote_test.databinding.FragmentAdminGamblerPhotoBinding
import com.example.tote_test.utils.loadImage

class AdminGamblerPhotoFragment : Fragment() {
    private lateinit var binding: FragmentAdminGamblerPhotoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val photoUrl = arguments?.getString("photoUrl")
        /*val photoUrl = AdminGamblerPhotoFragmentArgs.fromBundle(arguments).photoUrl*/

        binding = FragmentAdminGamblerPhotoBinding.inflate(layoutInflater, container, false)

        binding.adminGamblerPhotoFull.loadImage(photoUrl!!)

        return binding.root
    }
}