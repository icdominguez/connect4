package com.icdominguez.connect4.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.icdominguez.connect4.R
import com.icdominguez.connect4.databinding.FragmentWinBinding
import com.icdominguez.connect4.interfaces.OnRestartGameListener


class WinFragment : DialogFragment() {

    private lateinit var _binding: FragmentWinBinding
    private val binding get() = _binding

    var onContinueAcceptClick: OnRestartGameListener? = null

    fun setListener(onContinueAcceptClick: OnRestartGameListener) {
        this.onContinueAcceptClick = onContinueAcceptClick
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentWinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var color = if(arguments?.getInt("TURN") == 1) "#FF0000" else "#FFEB3B"

        binding.linearLayoutVictory.setBackgroundColor(Color.parseColor(color))

        if(color == "#FF0000") {
            binding.textViewPlayerWhoWins.text = getString(R.string.game_win_red)
        } else {
            binding.textViewPlayerWhoWins.text = getString(R.string.game_win_yellow)
        }

        binding.buttonAccept.setOnClickListener {
            this.dismiss()
            onContinueAcceptClick?.onAcceptClicked()
        }
    }

    companion object {
        var TAG = "WinFragmentDialog"

        fun newInstance(turn: Int) : WinFragment {
            val winFragment = WinFragment()
            val args = Bundle().apply {
                putInt("TURN", turn)
            }
            winFragment.arguments = args

            return winFragment
        }
    }
}