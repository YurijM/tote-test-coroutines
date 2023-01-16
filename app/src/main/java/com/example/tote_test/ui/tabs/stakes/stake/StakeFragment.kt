package com.example.tote_test.ui.tabs.stakes.stake

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tote_test.R
import com.example.tote_test.databinding.FragmentStakeBinding
import com.example.tote_test.models.GameModel
import com.example.tote_test.models.StakeModel
import com.example.tote_test.utils.*

class StakeFragment : Fragment() {
    private lateinit var binding: FragmentStakeBinding
    private val viewModel by viewModels<StakeViewModel>()

    private lateinit var game: GameModel
    private var isGroup = true

    private lateinit var inputGoal1: TextView
    private lateinit var inputGoal2: TextView
    private lateinit var inputAddGoal1: TextView
    private lateinit var inputAddGoal2: TextView
    private var penaltyGame = ""

    private var isFirstShow = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStakeBinding.inflate(layoutInflater, container, false)

        game = StakeFragmentArgs.fromBundle(requireArguments()).game

        binding.stakeSave.setOnClickListener {
            saveStake()
        }

        isGroup = GROUPS.first { it.group == game.group }.number <= GROUPS_COUNT

        initFields()

        observeStatus()

        return binding.root
    }

    private fun initFields() {
        initFieldGoal1()
        initFieldGoal2()
        initFieldAddGoal1()
        initFieldAddGoal2()
        initFieldPenalty()

        setStartValues()

        isFirstShow = false
    }

    private fun setStartValues() {
        with(binding) {
            stakeStart.text = APP_ACTIVITY.getString(
                R.string.start_game,
                game.start.asTime(false),
                game.id.toString()
            )

            stakeTeam1.text = game.team1
            stakeTeam2.text = game.team2

            inputGoal1.text = game.goal1
            inputGoal2.text = game.goal2

            inputAddGoal1.text = game.addGoal1
            inputAddGoal2.text = game.addGoal2

            stakePenaltyTeam1.text = game.team1
            stakePenaltyTeam2.text = game.team2

            binding.stakePenaltyTeamsGroup.check(
                if (game.team1 == game.penalty) {
                    binding.stakePenaltyTeam1.id
                } else if (game.team2 == game.penalty) {
                    binding.stakePenaltyTeam2.id
                } else 0
            )
        }
    }

    private val watcherGoal1 = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null) {
                viewModel.changeGoal1(s.toString())
                game.goal1 = s.toString()

                binding.stakeSave.isEnabled = checkResultForEnabled()
                checkResultForAddTimeGone(isFirstShow)
            }
        }
    }

    private val watcherGoal2 = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null) {
                viewModel.changeGoal2(s.toString())
                game.goal2 = s.toString()

                binding.stakeSave.isEnabled = checkResultForEnabled()
                checkResultForAddTimeGone(isFirstShow)
            }
        }
    }

    private val watcherAddGoal1 = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null) {
                viewModel.changeAddGoal1(s.toString())
                game.addGoal1 = s.toString()

                binding.stakeSave.isEnabled = checkResultForEnabled()
                checkResultForPenaltyGone(isFirstShow)
            }
        }
    }

    private val watcherAddGoal2 = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null) {
                viewModel.changeAddGoal2(s.toString())
                game.addGoal2 = s.toString()

                binding.stakeSave.isEnabled = checkResultForEnabled()
                checkResultForPenaltyGone()
            }
        }
    }

    private fun initFieldGoal1() {
        inputGoal1 = binding.stakeInputGoal1
        viewModel.changeGoal1(game.goal1)

        inputGoal1.addTextChangedListener(watcherGoal1)
    }

    private fun initFieldGoal2() {
        inputGoal2 = binding.stakeInputGoal2
        viewModel.changeGoal2(game.goal2)

        inputGoal2.addTextChangedListener(watcherGoal2)
    }

    private fun initFieldAddGoal1() {
        inputAddGoal1 = binding.stakeInputAddGoal1
        viewModel.changeAddGoal1(game.addGoal1)

        inputAddGoal1.addTextChangedListener(watcherAddGoal1)
    }

    private fun initFieldAddGoal2() {
        inputAddGoal2 = binding.stakeInputAddGoal2
        viewModel.changeAddGoal2(game.addGoal2)

        inputAddGoal2.addTextChangedListener(watcherAddGoal2)
    }

    private fun initFieldPenalty() {
        viewModel.changePenalty(game.penalty)

        binding.stakePenaltyTeamsGroup.setOnCheckedChangeListener { _, checkedId ->
            penaltyGame = when (checkedId) {
                binding.stakePenaltyTeam1.id -> game.team1
                binding.stakePenaltyTeam2.id -> game.team2
                else -> ""
            }

            viewModel.changePenalty(penaltyGame)
            game.penalty = penaltyGame

            binding.stakeSave.isEnabled = checkResultForEnabled()
        }
    }

    private fun checkResultForEnabled(): Boolean {
        val isResult = inputGoal1.text.toString() != "" && inputGoal2.text.toString() != ""
        if (isGroup) {
            return isResult
        } else {
            val isAddTimeResult = inputAddGoal1.text.toString() != "" && inputAddGoal2.text.toString() != ""
            return if (isResult && inputGoal1.text.toString() == inputGoal2.text.toString()) {
                if (isAddTimeResult) {
                    if (inputAddGoal1.text.toString() == inputAddGoal2.text.toString()) {
                        binding.stakePenaltyTeamsGroup.checkedRadioButtonId > 0
                    } else {
                        true
                    }
                } else {
                    false
                }
            } else {
                isResult
            }
        }
    }

    private fun checkResultForAddTimeGone(isFirstShow: Boolean = false) {
        val result = inputGoal1.text.toString().isNotBlank() && inputGoal2.text.toString().isNotBlank()

        val isResultDraw = (!isGroup && result && inputGoal1.text.toString() == inputGoal2.text.toString())
        binding.stakeLayoutAddTime.isGone = !isResultDraw

        if (!isResultDraw && !isFirstShow) {
            inputAddGoal1.removeTextChangedListener(watcherAddGoal1)
            inputAddGoal2.removeTextChangedListener(watcherAddGoal2)

            inputAddGoal1.text = ""
            game.addGoal1 = ""
            inputAddGoal2.text = ""
            game.addGoal2 = ""
            binding.stakePenaltyTeamsGroup.check(0)
            game.penalty = ""
            binding.stakeLayoutPenalty.isGone = true

            inputAddGoal1.addTextChangedListener(watcherAddGoal1)
            inputAddGoal2.addTextChangedListener(watcherAddGoal2)
        }
    }

    private fun checkResultForPenaltyGone(isFirstShow: Boolean = false) {
        val resultAddTime = inputAddGoal1.text.toString().isNotBlank() && inputAddGoal2.text.toString().isNotBlank()

        val isResultAddTimeDraw = (resultAddTime && inputAddGoal1.text.toString() == inputAddGoal2.text.toString())
        binding.stakeLayoutPenalty.isGone = !isResultAddTimeDraw

        if (!isResultAddTimeDraw && !isFirstShow) {
            binding.stakePenaltyTeamsGroup.check(0)
            game.penalty = ""
        }
    }

    private fun saveStake() {
        val stake = StakeModel()

        stake.gameId = game.id
        stake.gamblerId = CURRENT_ID
        stake.goal1 = game.goal1
        stake.goal2 = game.goal2
        stake.addGoal1 = game.addGoal1
        stake.addGoal2 = game.addGoal2
        stake.penalty = game.penalty
        stake.points = 0.0
        stake.place = 0

        viewModel.saveStake(stake)
    }

    private fun observeStatus() = viewModel.status.observe(viewLifecycleOwner) {
        when (it) {
            is Resource.Loading -> {
                binding.stakeProgressBar.isVisible = true
            }
            is Resource.Success -> {
                binding.stakeProgressBar.isInvisible = true
                findTopNavController().navigate(R.id.action_stakeFragment_to_stakesFragment)
            }
            is Resource.Error -> {
                binding.stakeProgressBar.isInvisible = true
                fixError(it.message.toString())
            }
        }
    }
}