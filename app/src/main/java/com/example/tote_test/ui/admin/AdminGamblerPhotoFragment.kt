package com.example.tote_test.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tote_test.databinding.FragmentAdminGamblerPhotoBinding
import com.example.tote_test.utils.getSizeDisplay
import com.example.tote_test.utils.loadImage


class AdminGamblerPhotoFragment : Fragment() {
    private lateinit var binding: FragmentAdminGamblerPhotoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /*val photoUrl = arguments?.getString("photoUrl")
        val isBottomNav = arguments?.getBoolean("isBottomNav")*/

        //AdminGamblerPhotoFragmentArgs.fromBundle(arguments).photoUrl as String

        val photoUrl = AdminGamblerPhotoFragmentArgs.fromBundle(requireArguments()).photoUrl
        val isBottomNav = AdminGamblerPhotoFragmentArgs.fromBundle(requireArguments()).isBottomNav

        binding = FragmentAdminGamblerPhotoBinding.inflate(layoutInflater, container, false)

        //val size = isBottomNav?.let { getSizeDisplay(it) }
        val size = getSizeDisplay(isBottomNav)

        /*if (photoUrl != null) {
            binding.adminGamblerPhotoFull.loadImage(photoUrl, size?.get(0) ?: 100, size?.get(1) ?: 100)
        }*/
        binding.adminGamblerPhotoFull.loadImage(photoUrl, size[0], size[1])

        return binding.root
    }
}