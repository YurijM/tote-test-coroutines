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

    private var isNicknameFilled = false
    private var isFamilyFilled = false
    private var isNameFilled = false
    private var isGenderFilled = false
    private var isPhotoUriFilled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        toLog("${javaClass.simpleName} - ${object {}.javaClass.enclosingMethod?.name}")

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        observeProfile()
        observePhotoUri()
        observeStatus()

        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        initFields()

        binding.profileLoadPhoto.setOnClickListener {
            getImage()
        }

        binding.profileSave.setOnClickListener {
            saveProfile()
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let {
                    it.path?.let { path -> loadProfilePhoto(path) }
                    viewModel.changePhotoUrl(it)
                }

                //result.data?.data?.let { viewModel.changePhotoUri(it) }
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
        //initFieldPhotoUri()
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

                isNicknameFilled =
                    !checkFieldBlank(it.toString(), binding.profileLayoutNickname, getString(R.string.nickname))

                binding.profileSave.isEnabled = isFieldsFilled()
            }
        }
    }

    private fun initFieldFamily() {
        inputFamily = binding.profileInputFamily

        inputFamily.addTextChangedListener {
            if (it != null) {
                viewModel.changeFamily(it.toString())

                isFamilyFilled = !checkFieldBlank(it.toString(), binding.profileLayoutFamily, getString(R.string.family))

                binding.profileSave.isEnabled = isFieldsFilled()
            }
        }
    }

    private fun initFieldName() {
        inputName = binding.profileInputName

        inputName.addTextChangedListener {
            if (it != null) {
                viewModel.changeName(it.toString())

                isNameFilled = !checkFieldBlank(it.toString(), binding.profileLayoutName, getString(R.string.name))

                binding.profileSave.isEnabled = isFieldsFilled()
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

            isGenderFilled = gender.isNotBlank()

            binding.profileErrorGender.visibility = if (isGenderFilled) View.GONE else View.VISIBLE

            binding.profileSave.isEnabled = isFieldsFilled()
        }
    }

    private fun initFieldPhotoUri() {
        isPhotoUriFilled = binding.profilePhoto.tag != EMPTY

        binding.profileErrorPhoto.visibility = if (isPhotoUriFilled) View.GONE else View.VISIBLE

        binding.profileSave.isEnabled = isFieldsFilled()
    }

    private fun loadProfilePhoto(photoUrl: String) {
        val size = resources.getDimensionPixelSize(R.dimen.profile_size_photo)
        val radius = resources.getDimensionPixelSize(R.dimen.profile_size_photo_radius)
        binding.profilePhoto.loadImage(photoUrl, size, size, radius)

        binding.profilePhoto.tag = photoUrl

        initFieldPhotoUri()
    }

    private fun isFieldsFilled(): Boolean =
        isNicknameFilled
                && isFamilyFilled
                && isNameFilled
                && isGenderFilled
                && isPhotoUriFilled

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

        toLog("observeProfile -> binding.profilePhoto.tag: ${binding.profilePhoto.tag}")
        toLog("observeProfile -> profile.photoUrl: ${it.photoUrl}")

        if (binding.profilePhoto.tag != EMPTY) {
            loadProfilePhoto(binding.profilePhoto.tag.toString())
        } else if (it.photoUrl != EMPTY) {
            loadProfilePhoto(it.photoUrl)
        }

        //initFieldPhotoUri()
    }

    private fun observePhotoUri() = viewModel.photoUri.observe(viewLifecycleOwner) {
        toLog("observePhotoUri -> path: $it")
        loadProfilePhoto(it.toString())

        initFieldPhotoUri()
    }

    /*private fun observeInProgress() = viewModel.inProgress.observe(viewLifecycleOwner) {
        if (it) {
            binding.profileProgressBar.visibility = View.VISIBLE
        } else {
            binding.profileProgressBar.visibility = View.INVISIBLE
        }

        binding.profileSave.isEnabled = (!it && isFieldsFilled())
    }*/

    /*private fun saveProfilePhoto() {
        //val tag = binding.profilePhoto.tag.toString()

        //toLog("saveProfilePhoto -> tag: $tag")
        toLog("saveProfilePhoto -> GAMBLER.photoUrl: ${GAMBLER.photoUrl}")
        //if (tag.isNotBlank() && tag != EMPTY) {
            viewModel.saveImageToStorage {
                binding.profilePhoto.tag = it

                toLog("saveProfilePhoto")
                showToast("Сохранено")

                //toGamblers()
            }
        //}
    }*/

    /*private fun saveProfilePhoto() {
        val tag = binding.profilePhoto.tag.toString()

        toLog("saveProfilePhoto -> tag: $tag")
        toLog("saveProfilePhoto -> GAMBLER.photoUrl: ${GAMBLER.photoUrl}")
        if (tag.isNotBlank() && tag != EMPTY) {
            viewModel.saveImageToStorage(
                {
                    viewModel.getUrlFromStorage
                    {
                        viewModel.savePhotoUrlToDB(
                            it,
                            {
                                binding.profilePhoto.tag = it

                                viewModel.hideProgress()

                                toLog("saveProfilePhoto")
                                showToast("Сохранено")
                            }
                    },
                    {}
                    )
                },
                {
                    toLog("saveProfilePhoto -> saveImageToStorage -> fail: $it")
                }
            )
        }
    }*/

    private fun saveProfile() {
        /*if (isNicknameFilled
            && isFamilyFilled
            && isNameFilled
            && isGenderFilled
        ) {*/
            viewModel.profile.value?.let { viewModel.saveProfile(it) }
        //}
    }

    private fun toGamblers() {
        if (viewModel.checkProfileFilled()) {
            //findTopNavController().navigate(R.id.action_profileFragment_to_tabsFragment)
        }
    }

    /*private fun saveProfile() {
        if (isNicknameFilled
            && isFamilyFilled
            && isNameFilled
            && isGenderFilled
        ) {
            //viewModel.showProgress()

            viewModel.saveGamblerToDB {
                toLog("saveProfile")

                val tag = binding.profilePhoto.tag.toString()
                toLog("saveProfile -> tag: $tag")
                if (tag.isNotBlank() && tag != EMPTY) {
                    saveProfilePhoto()
                }

                //GAMBLER = localGambler
                //APP_ACTIVITY.viewModel.changeGambler(GAMBLER)
                toLog("ProfileFragment -> saveProfile -> GAMBLER: $GAMBLER")

                toGamblers()
            }
            //viewModel.hideProgress()
        }
    }*/

    private fun observeStatus() = viewModel.status.observe(viewLifecycleOwner) {
        when (it) {
            is Resource.Loading -> {
                binding.profileProgressBar.isVisible = true
            }
            is Resource.Success -> {
                binding.profileProgressBar.isVisible = false

                /*CURRENT_ID = AUTH.currentUser?.uid.toString()
                if (CURRENT_ID.isNotBlank()) {
                    AppPreferences.setIsAuth(true)

                    lifecycleScope.launch(Dispatchers.IO) {
                        GAMBLER = REPOSITORY.getGambler(CURRENT_ID)

                        withContext(Dispatchers.Main) {
                            loadAppBarPhoto()
                            toLog("GAMBLER: $GAMBLER")

                            if (isProfileFilled(GAMBLER)) {
                                findTopNavController().navigate(R.id.action_signInFragment_to_tabsFragment)
                            } else {
                                findTopNavController().navigate(R.id.action_signInFragment_to_profileFragment)
                            }
                        }
                    }
                }*/
            }
            is Resource.Error -> {
                binding.profileProgressBar.isVisible = false
                fixError(it.message.toString())
            }
        }
    }
}