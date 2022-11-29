package hu.bme.aut.android.hf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.hf.adapter.DeckAdapter
import hu.bme.aut.android.hf.data.Deck
import hu.bme.aut.android.hf.data.FlashCardDatabase
import hu.bme.aut.android.hf.databinding.ActivityDeckBinding
import hu.bme.aut.android.hf.fragments.NewDeckDialogFragment
import kotlin.concurrent.thread

class DeckActivity : AppCompatActivity(), DeckAdapter.DeckClickListener, NewDeckDialogFragment.NewDeckDialogListener {
    private lateinit var binding: ActivityDeckBinding

    private lateinit var database: FlashCardDatabase
    private lateinit var adapter: DeckAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeckBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        database = FlashCardDatabase.getDatabase(applicationContext)

        binding.fab.setOnClickListener {
            NewDeckDialogFragment().show(
                supportFragmentManager,
                NewDeckDialogFragment.TAG
            )
        }


        initRecyclerView()
    }
    private fun initRecyclerView() {
        adapter = DeckAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.allDao().getAllDeck()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onItemChanged(item: Deck) {
        thread {
            database.allDao().updateDeck(item)
            Log.d("DeckActivity", "Deck update was successful")
        }
    }

    override fun onItemDeleted(item: Deck) {
        thread {
            database.allDao().deleteItemDeck(item)
            runOnUiThread {
                adapter.deleteItem(item)
            }
            Log.d("DeckActivity", "Deck delete was successful")
        }
    }

    override fun onItemOpened(deckId: Long) {
        val intent = Intent(this, FlashCardActivity::class.java)
        intent.putExtra(FlashCardActivity.DECK_ID, deckId)
        startActivity(intent)
    }

    override fun onDeckCreated(deck: Deck) {
        thread {
            if(database.allDao().isUnique(deck.name) == 0) {
                val insertId = database.allDao().insertDeck(deck)
                deck.id = insertId
                runOnUiThread {
                    adapter.addItem(deck)
                }
            }
        }
    }
}