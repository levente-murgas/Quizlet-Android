package hu.bme.aut.android.hf.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.hf.FlashCardActivity
import hu.bme.aut.android.hf.QuizActivity
import hu.bme.aut.android.hf.R
import hu.bme.aut.android.hf.databinding.DialogQuizFinishedBinding


class QuizFinishedDialogFragment : DialogFragment() {

    private lateinit var binding: DialogQuizFinishedBinding
    private var numberOfCards = -1;
    private var deckId: Long = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        numberOfCards = arguments?.getInt("cardNum")!!
        deckId = arguments?.getLong("deckId")!!
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogQuizFinishedBinding.inflate(LayoutInflater.from(context))

        if(numberOfCards != 0){
            binding.tvMessage.setText(R.string.quiz_finished_cards_left)

            return AlertDialog.Builder(requireContext())
                .setTitle(R.string.quiz_finished)
                .setView(binding.root)
                .setPositiveButton(R.string.button_yes) { dialogInterface, i ->
                    val intent = Intent(context, QuizActivity::class.java)
                    intent.putExtra(QuizActivity.DECK_ID, deckId)
                    intent.putExtra(QuizActivity.STUDY_AGAIN, true)
                    context?.startActivity(intent)
                    activity?.finish()
                }
                .setNegativeButton(R.string.button_no) { dialogInterface, i ->
                    val intent = Intent(context, FlashCardActivity::class.java)
                    intent.putExtra(FlashCardActivity.DECK_ID, deckId)
                    context?.startActivity(intent)
                    activity?.finish()
                }
                .create()
        }
        else{
            binding.tvMessage.setText(R.string.quiz_finished_no_cards_left)

            return AlertDialog.Builder(requireContext())
                .setTitle(R.string.quiz_finished)
                .setView(binding.root)
                .setPositiveButton(R.string.button_ok) { dialogInterface, i ->
                    val intent = Intent(context, FlashCardActivity::class.java)
                    intent.putExtra(FlashCardActivity.DECK_ID, deckId)
                    context?.startActivity(intent)
                    activity?.finish()
                }
                .create()
        }
    }


    companion object {
        fun newInstance(cardNum: Int, deckId: Long): QuizFinishedDialogFragment? {
            val f = QuizFinishedDialogFragment()

            // Supply parameters as an argument.
            val args = Bundle()
            args.putInt("cardNum", cardNum)
            args.putLong("deckId", deckId)
            f.arguments = args
            return f
        }

        const val TAG = "QuizFinishedDialogFragment"
    }
}