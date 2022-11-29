package hu.bme.aut.android.hf

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.LinearLayout
import android.widget.TextView
import hu.bme.aut.android.hf.data.FlashCard
import hu.bme.aut.android.hf.data.FlashCardDatabase
import hu.bme.aut.android.hf.databinding.ActivityQuizBinding
import hu.bme.aut.android.hf.fragments.NewDeckDialogFragment
import hu.bme.aut.android.hf.fragments.QuizFinishedDialogFragment
import kotlin.concurrent.thread


class QuizActivity : AppCompatActivity() {

    companion object {
        const val DECK_ID = "DECK_ID"
        const val STUDY_AGAIN = "STUDY_AGAIN"
        private const val AUTO_HIDE = true
        private const val AUTO_HIDE_DELAY_MILLIS = 3000
        private const val UI_ANIMATION_DELAY = 300
    }

    private lateinit var binding: ActivityQuizBinding
    private lateinit var fullscreenContent: TextView
    private lateinit var fullscreenContentControls: LinearLayout
    private val hideHandler = Handler(Looper.myLooper()!!)
    private var isFullscreen: Boolean = false

    private var termDisplayed: Boolean = true
    private var cardIndex = 0;
    private var deckId: Long = -1;
    private var studyAgain: Boolean = false;
    private lateinit var database: FlashCardDatabase
    private var items = mutableListOf<FlashCard>()



    @SuppressLint("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar
        if (Build.VERSION.SDK_INT >= 30) {
            fullscreenContent.windowInsetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            fullscreenContent.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }
    private val showPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
        fullscreenContentControls.visibility = View.VISIBLE
    }

    private val hideRunnable = Runnable { hide() }




    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FlashCardDatabase.getDatabase(applicationContext)

        loadItemsInBackground()


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        isFullscreen = true

        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent = binding.fullscreenContent
        fullscreenContent.setOnClickListener {
            if(termDisplayed){
                binding.fullscreenContent.text = items[cardIndex].definition
                termDisplayed = false
            }
            else{
                binding.fullscreenContent.text = items[cardIndex].term
                termDisplayed = true
            }
        }

        fullscreenContentControls = binding.fullscreenContentControls

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        binding.btnGotIt.setOnClickListener {
            items[cardIndex].studyAgain = false
            onItemChanged(items[cardIndex])
            termDisplayed = true
            if(cardIndex != items.size - 1){
                binding.fullscreenContent.text = items[++cardIndex].term
            }
            else {
                thread {
                    val studyAgains =
                        database.allDao().getStudyAgainsFromDeck(deck_id = deckId).toMutableList()
                    runOnUiThread {
                        QuizFinishedDialogFragment.newInstance(studyAgains.size,deckId)?.show(
                            supportFragmentManager,
                            QuizFinishedDialogFragment.TAG
                        )
                    }
                }
            }
        }

        binding.btnStudyAgain.setOnClickListener {
            items[cardIndex].studyAgain = true
            onItemChanged(items[cardIndex])
            termDisplayed = true
            if(cardIndex != items.size - 1) {
                binding.fullscreenContent.text = items[++cardIndex].term
            }
            else {
                thread {
                    val studyAgains =
                        database.allDao().getStudyAgainsFromDeck(deck_id = deckId).toMutableList()
                    runOnUiThread {
                        QuizFinishedDialogFragment.newInstance(studyAgains.size,deckId)?.show(
                            supportFragmentManager,
                            QuizFinishedDialogFragment.TAG
                        )
                    }
                }
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, 0)
    }


    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        fullscreenContentControls.visibility = View.VISIBLE
        isFullscreen = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable)
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    fun onItemChanged(item: FlashCard) {
        thread {
            database.allDao().updateCard(item)
            Log.d("FlashCardActivity", "FlashCard update was successful")
        }
    }

    private fun loadItemsInBackground() {
        thread {
            deckId = this.intent.getLongExtra(QuizActivity.DECK_ID, -1)
            studyAgain = this.intent.getBooleanExtra(QuizActivity.STUDY_AGAIN,false)
            if(!studyAgain) {
                items = database.allDao().getDeckWithCards(deckId).toMutableList()
            }
            else {
                items = database.allDao().getStudyAgainsFromDeck(deckId).toMutableList()
                studyAgain = false
            }
            binding.fullscreenContent.text = items[cardIndex].term
        }
    }


}