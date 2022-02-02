package com.icdominguez.connect4.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.icdominguez.connect4.R
import com.icdominguez.connect4.databinding.FragmentGameBinding
import com.icdominguez.connect4.interfaces.OnRestartGameListener
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class GameFragment : Fragment() {

    private lateinit var _binding: FragmentGameBinding
    private val binding get() = _binding

    private var turn = 1
    private var winsPlayerOne = 0
    private var winsPlayerTwo = 0

    private var moves = 0

    companion object {
        var RANGE_COLUMNS = 1..7
        var RANGE_ROWS = 1..6

        var MIN_MOVES = 7
        var MAX_MOVES = 42
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun changeTurn() {
        turn = if (turn == 1) 2 else 1
        when(turn) {
            1 -> {
                binding.imageViewTurnRed.setImageResource(R.drawable.red_win)
                binding.imageViewTurnYellow.setImageResource(R.drawable.yellow_disc)
            }
            2 -> {
                binding.imageViewTurnYellow.setImageResource(R.drawable.yellow_win)
                binding.imageViewTurnRed.setImageResource(R.drawable.red_disc)
            }
        }
    }

    private fun setPiece(tag: String) {
        var imageView = "${RANGE_ROWS.last}${tag.substring(tag.lastIndex)}".toInt()

        while (imageView >= 11) {
            if((binding.linearLayoutTable).findViewWithTag<ImageView>("imageView$imageView").drawable.constantState == requireActivity().getDrawable(R.drawable.empty_disc)!!.constantState) {
                break
            } else {
                imageView -= 10
            }
        }


        if(imageView >= 11) {
            when (turn) {
                1 -> (binding.linearLayoutTable).findViewWithTag<ImageView>("imageView$imageView").setImageResource(R.drawable.red_disc)
                2 -> (binding.linearLayoutTable).findViewWithTag<ImageView>("imageView$imageView").setImageResource(R.drawable.yellow_disc)
            }

            if(moves > MIN_MOVES) {
                if(checkWin(imageView)) {

                    var party = Party(speed = 0f, maxSpeed = 30f, damping = 0.9f, spread = 360, colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def), emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100), position = Position.Relative(0.5, 0.3))
                    binding.konfettiView.start(party)

                    if(turn == 1) winsPlayerOne += 1 else winsPlayerTwo += 1

                    updateScore()

                    openFragmentWin()
                }
            }

            changeTurn()

            moves += 1

            if(moves == MAX_MOVES) restartGame()
        }
    }

    private fun checkWin(imageView: Int) : Boolean {
        return checkVerticalWin(imageView) || checkHorizontalWin(imageView) || checkDiagonalLeftWin(imageView) || checkDiagonalRightWin(imageView)
    }

    private fun openFragmentWin() {
        val winFragment = WinFragment.newInstance(turn)
        winFragment.show(parentFragmentManager, WinFragment.TAG)

        winFragment.setListener(object : OnRestartGameListener {
            override fun onAcceptClicked() {
                restartGame()
            }
        })
    }

    private fun checkDiagonalRightWin(imageView: Int) : Boolean {
        var drawable = if(turn == 1) R.drawable.red_disc else R.drawable.yellow_disc
        var win = false
        var arrayDiagonals: ArrayList<Int> = ArrayList()
        var arrayWins: ArrayList<Int> = ArrayList()
        var imageViewCounter = imageView

        do {
            if((binding.linearLayoutTable).findViewWithTag<ImageView>("imageView$imageViewCounter") != null) {
                arrayDiagonals.add(imageViewCounter)
            } else {
                break
            }
            imageViewCounter -= 9
        } while (imageViewCounter >= 11)

        imageViewCounter = imageView

        do {
            if((binding.linearLayoutTable).findViewWithTag<ImageView>("imageView$imageViewCounter") != null) {
                if(!arrayDiagonals.contains(imageViewCounter)) {
                    arrayDiagonals.add(imageViewCounter)
                }
            } else {
                break
            }

            imageViewCounter += 9

        } while (imageViewCounter < 68)

        for(i in 0 until arrayDiagonals.size) {
            if((binding.linearLayoutTable).findViewWithTag<ImageView>("imageView${arrayDiagonals[i]}") != null) {
                if((binding.linearLayoutTable).findViewWithTag<ImageView>("imageView${arrayDiagonals[i]}").drawable.constantState == requireActivity().getDrawable(drawable)!!.constantState) {
                    arrayWins.add(arrayDiagonals[i])
                } else {
                    arrayWins.clear()
                }

                if(arrayWins.size == 4) {
                    win = true
                    break
                }
            }
        }

        if(win) updateBackgroundWin(arrayWins.sorted() as ArrayList<Int>)

        return win
    }

    private fun checkDiagonalLeftWin(imageView: Int) : Boolean {
        var drawable = if(turn == 1) R.drawable.red_disc else R.drawable.yellow_disc
        var win = false
        var arrayDiagonals: ArrayList<Int> = ArrayList()
        var arrayWins: ArrayList<Int> = ArrayList()
        var imageViewCounter = imageView

        do {
            if((binding.linearLayoutTable).findViewWithTag<ImageView>("imageView$imageViewCounter") != null) {
                arrayDiagonals.add(imageViewCounter)
            } else {
                break
            }
            imageViewCounter -= 9
        } while (imageViewCounter >= 11)

        imageViewCounter = imageView

        do {
            if((binding.linearLayoutTable).findViewWithTag<ImageView>("imageView$imageViewCounter") != null) {
                if(!arrayDiagonals.contains(imageViewCounter)) {
                    arrayDiagonals.add(imageViewCounter)
                }
            } else {
                break
            }

            imageViewCounter += 9

        } while (imageViewCounter < 68)

        arrayDiagonals.sort()

        for(i in 0 until arrayDiagonals.size) {
            if((binding.linearLayoutTable).findViewWithTag<ImageView>("imageView${arrayDiagonals[i]}") != null) {
                if((binding.linearLayoutTable).findViewWithTag<ImageView>("imageView${arrayDiagonals[i]}").drawable.constantState == requireActivity().getDrawable(drawable)!!.constantState) {
                    arrayWins.add(arrayDiagonals[i])
                } else {
                    arrayWins.clear()
                }

                if(arrayWins.size == 4) {
                    win = true
                    break
                }
            }
        }

        if(win) updateBackgroundWin(arrayWins)

        return win
    }

    private fun checkHorizontalWin(imageView: Int): Boolean {
        var drawable = if(turn == 1) R.drawable.red_disc else R.drawable.yellow_disc
        var imageView = "${imageView.toString().take(1)}7".toInt()
        var win = false
        var arrayWins: ArrayList<Int> = ArrayList()

        for (i in RANGE_COLUMNS) {
            if((binding.linearLayoutTable).findViewWithTag<ImageView>("imageView$imageView") != null) {
                if((binding.linearLayoutTable).findViewWithTag<ImageView>("imageView$imageView").drawable.constantState == requireActivity().getDrawable(drawable)!!.constantState) {
                    arrayWins.add(imageView)
                } else {
                    arrayWins.clear()
                }

                if(arrayWins.size == 4) {
                    win = true
                    break
                }
            }
            imageView -= 1
        }

        if(win) updateBackgroundWin(arrayWins)

        return win
    }

    private fun checkVerticalWin(imageView: Int): Boolean {
        var drawable = if(turn == 1) R.drawable.red_disc else R.drawable.yellow_disc
        var imageView = "${RANGE_ROWS.last}${imageView.toString().substring(imageView.toString().lastIndex)}".toInt()
        var arrayWins: ArrayList<Int> = ArrayList()
        var win = false

        for(i in RANGE_ROWS) {
            if((binding.linearLayoutTable).findViewWithTag<ImageView>("imageView$imageView") != null) {
                if((binding.linearLayoutTable).findViewWithTag<ImageView>("imageView$imageView").drawable.constantState == requireActivity().getDrawable(drawable)!!.constantState) {
                    arrayWins.add(imageView)
                } else {
                    arrayWins.clear()
                }

                if(arrayWins.size == 4) {
                    win = true
                    break
                }
            }
            imageView -= 10
        }

        if(win) updateBackgroundWin(arrayWins)

        return win
    }

    private fun updateBackgroundWin(arrayWins: ArrayList<Int>) {
        arrayWins.forEach {
            if(turn == 1) (binding.linearLayoutTable).findViewWithTag<ImageView>("imageView$it").setImageResource(R.drawable.red_win)
            else (binding.linearLayoutTable).findViewWithTag<ImageView>("imageView$it").setImageResource(R.drawable.yellow_win)
        }
    }

    private fun updateScore() {
        binding.textViewWinsPlayer1.text = winsPlayerOne.toString()
        binding.textViewWinsPlayer2.text = winsPlayerTwo.toString()
    }

    private fun initViews() {
        binding.buttonRestartGame.setOnClickListener { restartGame() }
        binding.buttonRestartCounter.setOnClickListener { restartCounter() }

        for (i in RANGE_COLUMNS) {
            var imageViewArrow = ImageView(requireActivity())
            binding.linearLayoutArrows.addView(imageViewArrow)

            imageViewArrow.layoutParams.width = 120
            imageViewArrow.layoutParams.height = 120
            imageViewArrow.setPadding(10,10,10,10)
            imageViewArrow.setImageResource(R.drawable.ic_arrow)
            imageViewArrow.tag = "arrow$i"

            imageViewArrow.setOnClickListener {
                setPiece(it.tag.toString())
            }
        }

        for (x in RANGE_ROWS) {
            var linearLayout = LinearLayout(requireContext())
            binding.linearLayoutTable.addView(linearLayout)
            for(y in RANGE_COLUMNS) {
                var imageView = ImageView(requireActivity())
                linearLayout.addView(imageView)

                imageView.layoutParams.width = 120
                imageView.layoutParams.height = 120

                imageView.setPadding(10,0,10,0)
                imageView.setImageResource(R.drawable.empty_disc)
                imageView.tag = "imageView$x$y"
            }
        }
    }

    fun restartGame() {
        binding.linearLayoutTable.removeAllViews()
        binding.linearLayoutArrows.removeAllViews()
        initViews()
        turn = 1
        binding.imageViewTurnRed.setImageResource(R.drawable.red_win)
        binding.imageViewTurnYellow.setImageResource(R.drawable.yellow_disc)
        moves = 0
    }

    private fun restartCounter() {
        winsPlayerTwo = 0
        winsPlayerOne = 0
        updateScore()
    }

}