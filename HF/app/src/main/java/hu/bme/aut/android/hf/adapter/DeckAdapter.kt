package hu.bme.aut.android.hf.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.hf.data.Deck
import hu.bme.aut.android.hf.databinding.ItemDeckListBinding

class DeckAdapter(private val listener: DeckClickListener) :
    RecyclerView.Adapter<DeckAdapter.DeckViewHolder>() {

    private val items = mutableListOf<Deck>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DeckViewHolder(
        ItemDeckListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: DeckViewHolder, position: Int) {
        val deck = items[position]

        holder.binding.btnName.text = deck.name

        holder.binding.ibRemove.setOnClickListener {
            listener.onItemDeleted(deck)
        }

        holder.binding.btnName.setOnClickListener {
            listener.onItemOpened(deck.id!!)
        }
    }

    override fun getItemCount(): Int = items.size

    interface DeckClickListener {
        fun onItemChanged(item: Deck)
        fun onItemDeleted(item: Deck)
        fun onItemOpened(deckId: Long)
    }

    fun addItem(item: Deck) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(shoppingItems: List<Deck>) {
        items.clear()
        items.addAll(shoppingItems)
        notifyDataSetChanged()
    }

    fun deleteItem(item: Deck) {
        val position = items.indexOf(item)
        items.remove(item)
        notifyItemRemoved(position)
    }

    inner class DeckViewHolder(val binding: ItemDeckListBinding) : RecyclerView.ViewHolder(binding.root)
}