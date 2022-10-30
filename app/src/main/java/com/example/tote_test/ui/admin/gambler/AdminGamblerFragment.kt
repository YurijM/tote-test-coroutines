package com.example.tote_test.ui.admin.gambler

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentAdminGamblerBinding
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.*
import java.lang.Integer.parseInt

class AdminGamblerFragment : Fragment() {
    private lateinit var binding: FragmentAdminGamblerBinding
    private lateinit var viewModel: AdminGamblerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        //val gambler = arguments?.getSerializable("gambler") as GamblerModel

        viewModel = ViewModelProvider(this)[AdminGamblerViewModel::class.java]
        observeStatus()

        binding = FragmentAdminGamblerBinding.inflate(layoutInflater, container, false)

        val gambler = AdminGamblerFragmentArgs.fromBundle(requireArguments()).gambler

        binding.adminGamblerPhoto.setOnClickListener {
            /*val bundle = Bundle()
            bundle.putString("photoUrl", gambler.photoUrl)
            bundle.putBoolean("isBottomNav", true)

            findTopNavController().navigate(R.id.action_adminGamblerFragment_to_adminGamblerPhotoFragment, bundle)*/

            val action = AdminGamblerFragmentDirections.actionAdminGamblerFragmentToAdminGamblerPhotoFragment(
                gambler.photoUrl,
                true
            )
            findTopNavController().navigate(action)
        }

        binding.adminGamblerSave.setOnClickListener {
            gambler.stake = parseInt(binding.adminGamblerInputStake.text.toString())
            gambler.active = binding.adminGamblerActive.isChecked
            gambler.admin = binding.adminGamblerAdmin.isChecked

            viewModel.saveProfile(gambler)
        }

        setFields(gambler)

        return binding.root
    }

    private fun setFields(gambler: GamblerModel) {
        with(binding) {
            adminGamblerNickname.text = gambler.nickname.ifBlank {
                adminGamblerNickname.setTextColor(Color.parseColor("red"))
                adminGamblerNickname.setTypeface(null, Typeface.ITALIC)
                getString(R.string.error_value_empty, getString(R.string.nickname))
            }
            adminGamblerEmail.text = gambler.email.ifBlank {
                adminGamblerEmail.setTextColor(Color.parseColor("red"))
                adminGamblerEmail.setTypeface(null, Typeface.ITALIC)
                getString(R.string.error_value_empty, getString(R.string.email))
            }
            adminGamblerFamily.text = gambler.family.ifBlank {
                adminGamblerFamily.setTextColor(Color.parseColor("red"))
                adminGamblerFamily.setTypeface(null, Typeface.ITALIC)
                getString(R.string.error_value_empty, getString(R.string.family))
            }
            adminGamblerName.text = gambler.name.ifBlank {
                adminGamblerName.setTextColor(Color.parseColor("red"))
                adminGamblerName.setTypeface(null, Typeface.ITALIC)
                getString(R.string.error_value_empty, getString(R.string.name))
            }
            adminGamblerGender.text = gambler.gender.ifBlank {
                adminGamblerLabelGender.visibility = View.GONE
                adminGamblerGender.setTypeface(null, Typeface.ITALIC)
                adminGamblerGender.setTextColor(Color.parseColor("red"))
                getString(R.string.error_value_empty, getString(R.string.gender))
            }
            adminGamblerPhoto.loadImage(
                gambler.photoUrl,
                resources.getDimensionPixelSize(R.dimen.profile_size_photo),
                resources.getDimensionPixelSize(R.dimen.profile_size_photo),
                resources.getDimensionPixelSize(R.dimen.profile_size_photo_radius)
            )
            (adminGamblerInputStake as TextView).text = gambler.stake.toString()
            adminGamblerActive.isChecked = gambler.active
            adminGamblerAdmin.isChecked = gambler.admin
        }
    }

    private fun observeStatus() = viewModel.status.observe(viewLifecycleOwner) {
        when (it) {
            is Resource.Loading -> {
                binding.adminGamblerProgressBar.isVisible = true
            }
            is Resource.Success -> {
                binding.adminGamblerProgressBar.isInvisible = true
                findTopNavController().navigate(R.id.action_adminGamblerFragment_to_ratingFragment)
            }
            is Resource.Error -> {
                binding.adminGamblerProgressBar.isInvisible = true
                fixError(it.message.toString())
            }
        }
    }
}