package hu.bme.aut.android.hf.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.hf.data.Deck
import hu.bme.aut.android.hf.data.DeckWithCards
import hu.bme.aut.android.hf.data.FlashCard
import hu.bme.aut.android.hf.databinding.ItemFlashcardListBinding

class FlashCardAdapter(private val listener: FlashCardClickListener) :
    RecyclerView.Adapter<FlashCardAdapter.FlashCardViewHolder>() {

    private val items = mutableListOf<FlashCard>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FlashCardViewHolder(
        ItemFlashcardListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: FlashCardViewHolder, position: Int) {
        val flashCard = items[position]

        holder.binding.tvTerm.text = flashCard.term
        holder.binding.tvDefinition.text = flashCard.definition

        holder.binding.ibRemove.setOnClickListener {
            listener.onItemDeleted(flashCard)
        }
    }

    override fun getItemCount(): Int = items.size

    interface FlashCardClickListener {
        fun onItemChanged(item: FlashCard)
        fun onItemDeleted(item: FlashCard)
    }

    fun isEmpty(): Boolean{
        return items.isEmpty()
    }

    fun addItem(item: FlashCard) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun deleteItem(item: FlashCard) {
        val position = items.indexOf(item)
        items.remove(item)
        notifyItemRemoved(position)
    }

    fun update(flashcards: List<FlashCard>) {
        items.clear()
        items.addAll(flashcards)
        notifyDataSetChanged()
    }

    inner class FlashCardViewHolder(val binding: ItemFlashcardListBinding) : RecyclerView.ViewHolder(binding.root)
}