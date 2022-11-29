package hu.bme.aut.android.hf

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.hf.adapter.FlashCardAdapter
import hu.bme.aut.android.hf.data.FlashCard
import hu.bme.aut.android.hf.data.FlashCardDatabase
import hu.bme.aut.android.hf.databinding.ActivityFlashCardBinding
import hu.bme.aut.android.hf.fragments.NewFlashCardDialogFragment
import kotlin.concurrent.thread

class FlashCardActivity : AppCompatActivity(), FlashCardAdapter.FlashCardClickListener, NewFlashCardDialogFragment.NewFlashCardDialogListener {
    companion object{
            const val DECK_ID = "DECK_ID"
    }
    private lateinit var binding: ActivityFlashCardBinding
    private lateinit var database: FlashCardDatabase
    private lateinit var adapter: FlashCardAdapter
    private lateinit var deckName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlashCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        database = FlashCardDatabase.getDatabase(applicationContext)

        binding.fab.setOnClickListener {
            NewFlashCardDialogFragment().show(
                supportFragmentManager,
                NewFlashCardDialogFragment.TAG
            )
        }

        initRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_flashcard, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_quiz -> {
                val intent = Intent(this, QuizActivity::class.java)
                val deckId = this.intent.getLongExtra(DECK_ID,-1)
                intent.putExtra(QuizActivity.DECK_ID, deckId)
                startActivity(intent)
                this.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.getItem(0).isEnabled = !adapter.isEmpty()
        return true
    }

    private fun initRecyclerView() {
        adapter = FlashCardAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val deckId = this.intent.getLongExtra(DECK_ID,-1)
            deckName = database.allDao().getDeckById(deckId).name
            val items = database.allDao().getDeckWithCards(deckId)
            runOnUiThread {
                val toolbar = binding.toolbar
                toolbar.title = deckName
                adapter.update(items)
            }
        }
    }

    override fun onItemChanged(item: FlashCard) {
        thread {
            database.allDao().updateCard(item)
            Log.d("FlashCardActivity", "FlashCard update was successful")
        }
    }

    override fun onItemDeleted(item: FlashCard) {
        thread {
            database.allDao().deleteCard(item)
            runOnUiThread {
                adapter.deleteItem(item)
            }
            Log.d("FlashCardActivity", "Deck delete was successful")
        }
    }

    override fun onFlashCardCreated(flashcard: FlashCard) {
        thread {
            val deckId = this.intent.getLongExtra(DECK_ID,-1)
            flashcard.deck_id = deckId
            val insertId = database.allDao().insertCard(flashcard)
            flashcard.id = insertId
            runOnUiThread {
                adapter.addItem(flashcard)
            }
        }
    }
}