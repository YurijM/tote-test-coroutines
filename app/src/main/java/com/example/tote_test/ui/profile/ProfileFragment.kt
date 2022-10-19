package com.example.tote_test.ui.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentProfileBinding
import com.example.tote_test.utils.*

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var launcher: ActivityResultLauncher<Intent>

    private lateinit var inputNickname: TextView
    private lateinit var inputFamily: TextView
    private lateinit var inputName: TextView
    private var gender = ""

    private var isPhotoUriFilled = false

    private var currentPhotoUrl = EMPTY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //toLog("${javaClass.simpleName} - ${object {}.javaClass.enclosingMethod?.name}")

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        observeProfile()
        observeFieldsFilled()
        observePhotoUri()
        observeStatus()

        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        initFields()

        binding.profilePhoto.setOnClickListener {
            /*val bundle = Bundle()
            bundle.putString("photoUrl", GAMBLER.photoUrl)
            bundle.putBoolean("isBottomNav", false)

            findTopNavController().navigate(R.id.action_profileFragment_to_adminGamblerPhotoFragment, bundle)*/

            val action = ProfileFragmentDirections.actionProfileFragmentToAdminGamblerPhotoFragment(GAMBLER.photoUrl, false)
            findTopNavController().navigate(action)
        }

        binding.profileLoadPhoto.setOnClickListener {
            getImage()
        }

        binding.profileSave.setOnClickListener {
            saveProfile()
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let {
                    viewModel.changePhotoUrl(it)
                }
            }
        }

        return binding.root
    }

    private fun getImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        launcher.launch(intent)
    }

    private fun initFields() {
        initFieldStake()
        initFieldNickname()
        initFieldFamily()
        initFieldName()
        initFieldGender()
    }

    private fun initFieldStake() {
        binding.profileStake.addTextChangedListener {
            if (it != null) {
                val isStakeFilled = (GAMBLER.stake != 0)

                binding.profileErrorStake.visibility = if (isStakeFilled) View.GONE else View.VISIBLE
            }
        }
    }

    private fun initFieldNickname() {
        inputNickname = binding.profileInputNickname

        inputNickname.addTextChangedListener {
            if (it != null) {
                viewModel.changeNickname(it.toString())

                !checkFieldBlank(it.toString(), binding.profileLayoutNickname, getString(R.string.nickname))
            }
        }
    }

    private fun initFieldFamily() {
        inputFamily = binding.profileInputFamily

        inputFamily.addTextChangedListener {
            if (it != null) {
                viewModel.changeFamily(it.toString())

                !checkFieldBlank(it.toString(), binding.profileLayoutFamily, getString(R.string.family))
            }
        }
    }

    private fun initFieldName() {
        inputName = binding.profileInputName

        inputName.addTextChangedListener {
            if (it != null) {
                viewModel.changeName(it.toString())

                !checkFieldBlank(it.toString(), binding.profileLayoutName, getString(R.string.name))
            }
        }
    }

    private fun initFieldGender() {
        binding.profileGenderGroup.setOnCheckedChangeListener { _, checkedId ->
            gender = when (checkedId) {
                binding.profileMan.id -> resources.getString(R.string.man)
                binding.profileWoman.id -> resources.getString(R.string.woman)
                else -> ""
            }

            viewModel.changeGender(gender)

            binding.profileErrorGender.visibility =
                if (gender.isNotBlank()) View.GONE else View.VISIBLE
        }
    }

    private fun initFieldPhotoUri() {
        isPhotoUriFilled = (binding.profilePhoto.tag.toString() != EMPTY
                || viewModel.profile.value?.photoUrl != EMPTY)

        binding.profileErrorPhoto.visibility = if (isPhotoUriFilled) View.GONE else View.VISIBLE
    }

    private fun loadProfilePhoto(photoUrl: String) {
        val size = resources.getDimensionPixelSize(R.dimen.profile_size_photo)
        val radius = resources.getDimensionPixelSize(R.dimen.profile_size_photo_radius)
        binding.profilePhoto.loadImage(photoUrl, size, size, radius)

        initFieldPhotoUri()
    }

    private fun observeProfile() = viewModel.profile.observe(viewLifecycleOwner) {
        binding.profileEmail.text = it.email
        binding.profileStake.text = getString(R.string.stake, it.stake)
        binding.profilePoints.text = getString(R.string.points, it.points)

        inputNickname.text = it.nickname
        inputFamily.text = it.family
        inputName.text = it.name
        binding.profileGenderGroup.check(
            when (it.gender) {
                "м" -> binding.profileMan.id
                "ж" -> binding.profileWoman.id
                else -> 0
            }
        )

        //toLog("observeProfile -> binding.profilePhoto.tag: ${binding.profilePhoto.tag}")
        //toLog("observeProfile -> profile.photoUrl: ${it.photoUrl}")
        //toLog("observeProfile -> currentPhotoUrl: $currentPhotoUrl")

        if (binding.profilePhoto.tag.toString() == EMPTY && it.photoUrl != currentPhotoUrl) {
            loadProfilePhoto(it.photoUrl)
            currentPhotoUrl = it.photoUrl
            //toLog("Фото загружено")
        }
    }

    private fun observeFieldsFilled() = viewModel.isFieldsFilled.observe(viewLifecycleOwner) {
        binding.profileSave.isEnabled = it
    }

    private fun observePhotoUri() = viewModel.photoUri.observe(viewLifecycleOwner) {
        binding.profilePhoto.tag = it
        loadProfilePhoto(it.toString())
    }

    private fun saveProfile() {
        viewModel.profile.value?.let { viewModel.saveProfile(it) }
    }

    private fun observeStatus() = viewModel.status.observe(viewLifecycleOwner) {
        when (it) {
            is Resource.Loading -> {
                binding.profileProgressBar.isVisible = true
            }
            is Resource.Success -> {
                binding.profileProgressBar.isInvisible = true

                if (binding.profilePhoto.tag.toString() != EMPTY) {
                    loadAppBarPhoto()
                    binding.profilePhoto.tag = EMPTY
                    currentPhotoUrl = viewModel.profile.value?.photoUrl ?: EMPTY
                }

                if (viewModel.checkProfileFilled()) {
                    findTopNavController().navigate(R.id.action_profileFragment_to_tabsFragment)
                }
            }
            is Resource.Error -> {
                binding.profileProgressBar.isInvisible = true
                fixError(it.message.toString())
            }
        }
    }
}