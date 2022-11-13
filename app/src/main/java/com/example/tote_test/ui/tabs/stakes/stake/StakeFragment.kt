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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStakeBinding.inflate(layoutInflater, container, false)

        binding.stakeSave.setOnClickListener {
            saveStake()
        }

        game = StakeFragmentArgs.fromBundle(requireArguments()).game

        isGroup = GROUPS.first { it.group == game.group }.number <= GROUPS_COUNT

        initFields()

        observeStatus()

        return binding.root
    }

    private fun initFields() {
        with(binding) {
            stakeStart.text = APP_ACTIVITY.getString(
                R.string.start_game,
                game.start.asTime(false),
                game.id.toString()
            )

            stakeTeam1.text = game.team1
            stakeTeam2.text = game.team2

            stakeInputGoal1.setText(game.goal1)
            stakeInputGoal2.setText(game.goal2)
            stakeInputAddGoal1.setText(game.addGoal1)
            stakeInputAddGoal2.setText(game.addGoal2)
            stakePenaltyTeam1.text = game.team1
            stakePenaltyTeam2.text = game.team2

            stakeLayoutAddTime.isGone = true
            stakeLayoutPenalty.isGone = true

            stakeSave.isEnabled = game.goal1.isNotBlank() && game.goal2.isNotBlank()
        }

        initFieldGoal1()
        initFieldGoal2()
        initFieldAddGoal1()
        initFieldAddGoal2()
        initFieldPenalty()
    }

    private val textWatcherGoal1 = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null) {
                viewModel.changeGoal1(s.toString())
                game.goal1 = s.toString()

                binding.stakeSave.isEnabled = checkResultForEnabled()
                checkResultForAddTimeGone()
            }
        }
    }

    private val textWatcherGoal2 = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null) {
                viewModel.changeGoal2(s.toString())
                game.goal2 = s.toString()

                binding.stakeSave.isEnabled = checkResultForEnabled()
                checkResultForAddTimeGone()
            }
        }
    }

    private val textWatcherAddGoal1 = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null) {
                viewModel.changeAddGoal1(s.toString())
                game.addGoal1 = s.toString()

                binding.stakeSave.isEnabled = checkResultForEnabled()
                checkResultForPenaltyGone()
            }
        }
    }

    private val textWatcherAddGoal2 = object : TextWatcher {
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

        inputGoal1.addTextChangedListener(textWatcherGoal1)
    }

    private fun initFieldGoal2() {
        inputGoal2 = binding.stakeInputGoal2
        viewModel.changeGoal2(game.goal2)

        inputGoal2.addTextChangedListener(textWatcherGoal2)
    }

    private fun initFieldAddGoal1() {
        inputAddGoal1 = binding.stakeInputAddGoal1
        viewModel.changeAddGoal1(game.addGoal1)

        inputAddGoal1.addTextChangedListener(textWatcherAddGoal1)
    }

    private fun initFieldAddGoal2() {
        inputAddGoal2 = binding.stakeInputAddGoal2
        viewModel.changeAddGoal2(game.addGoal2)

        inputAddGoal2.addTextChangedListener(textWatcherAddGoal2)
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
                        binding.stakePenalty.text != ""
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

    private fun checkResultForAddTimeGone() {
        val result = inputGoal1.text.toString().isNotBlank() && inputGoal2.text.toString().isNotBlank()

        val isResultDraw = (!isGroup && result && inputGoal1.text.toString() == inputGoal2.text.toString())
        binding.stakeLayoutAddTime.isGone = !isResultDraw

        if (!isResultDraw) {
            inputAddGoal1.removeTextChangedListener(textWatcherAddGoal1)
            inputAddGoal2.removeTextChangedListener(textWatcherAddGoal2)

            inputAddGoal1.text = ""
            game.addGoal1 = ""
            inputAddGoal2.text = ""
            game.addGoal2 = ""
            binding.stakePenaltyTeamsGroup.check(0)
            game.penalty = ""

            inputAddGoal1.addTextChangedListener(textWatcherAddGoal1)
            inputAddGoal2.addTextChangedListener(textWatcherAddGoal2)
        }
    }

    private fun checkResultForPenaltyGone() {
        val resultAddTime = inputAddGoal1.text.toString().isNotBlank() && inputAddGoal2.text.toString().isNotBlank()

        val isResultAddTimeDraw = (resultAddTime && inputAddGoal1.text.toString() == inputAddGoal2.text.toString())
        binding.stakeLayoutPenalty.isGone = !isResultAddTimeDraw

        if (!isResultAddTimeDraw) {
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

        viewModel.saveStake(stake)
    }

    private fun observeStatus() = viewModel.status.observe(viewLifecycleOwner) {
        when (it) {
            is Resource.Loading -> {
                binding.stakeProgressBar.isVisible = true
            }
            is Resource.Success -> {
                binding.stakeProgressBar.isInvisible = true
            }
            is Resource.Error -> {
                binding.stakeProgressBar.isInvisible = true
                fixError(it.message.toString())
            }
        }
    }
}