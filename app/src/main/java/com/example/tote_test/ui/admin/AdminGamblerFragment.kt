package com.example.tote_test.ui.admin

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentAdminGamblerBinding
import com.example.tote_test.models.GamblerModel
import com.example.tote_test.utils.APP_ACTIVITY
import com.example.tote_test.utils.loadImage

class AdminGamblerFragment : Fragment() {
    private lateinit var binding: FragmentAdminGamblerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val gambler = arguments?.getSerializable("gambler") as GamblerModel

        binding = FragmentAdminGamblerBinding.inflate(layoutInflater, container, false)

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

        return binding.root
    }
}