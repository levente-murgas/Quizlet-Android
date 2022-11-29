package hu.bme.aut.android.hf.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.hf.R
import hu.bme.aut.android.hf.data.FlashCard
import hu.bme.aut.android.hf.databinding.DialogNewFlashcardBinding


class NewFlashCardDialogFragment : DialogFragment() {
    interface NewFlashCardDialogListener {
        fun onFlashCardCreated(flashcard: FlashCard)
    }

    private lateinit var listener: NewFlashCardDialogListener

    private lateinit var binding: DialogNewFlashcardBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewFlashCardDialogListener
            ?: throw RuntimeException("Activity must implement the NewFlashCardDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogNewFlashcardBinding.inflate(LayoutInflater.from(context))

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.new_flash_card)
            .setView(binding.root)
            .setPositiveButton(R.string.button_ok) { dialogInterface, i ->
                if (isValid()) {
                    listener.onFlashCardCreated(getFlashCard())
                }
            }
            .setNegativeButton(R.string.button_cancel, null)
            .create()
    }

    private fun isValid() = binding.etTerm.text.isNotEmpty() && binding.etDefinition.text.isNotEmpty()

    private fun getFlashCard() = FlashCard(
        term = binding.etTerm.text.toString(),
        definition = binding.etDefinition.text.toString(),
        studyAgain = false
    )

    companion object {
        const val TAG = "NewFlashCardDialogFragment"
    }
}