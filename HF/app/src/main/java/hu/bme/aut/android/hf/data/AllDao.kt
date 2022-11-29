package hu.bme.aut.android.hf.data

import androidx.room.*

@Dao
interface AllDao {
    @Transaction
    @Query("SELECT * FROM flashcards " +
            "WHERE deck_id = :deck_id")
    fun getDeckWithCards(deck_id: Long): List<FlashCard>

    @Query("SELECT * FROM Deck")
    fun getAllDeck(): List<Deck>

    @Query("SELECT * FROM Deck WHERE id = :deckId")
    fun getDeckById(deckId: Long) : Deck

    @Query("SELECT * FROM flashcards " +
            "WHERE deck_id = :deck_id " +
            "AND study_again")
    fun getStudyAgainsFromDeck(deck_id: Long): List<FlashCard>

    @Query("SELECT COUNT (name) " +
            "FROM Deck " +
            "WHERE name = :deck_name")
    fun isUnique(deck_name: String): Int

    @Insert
    fun insertCard(flashCard: FlashCard): Long

    @Update
    fun updateCard(flashCard: FlashCard)

    @Delete
    fun deleteCard(flashCard: FlashCard)

    @Insert
    fun insertDeck(deck: Deck): Long

    @Update
    fun updateDeck(deck: Deck)

    @Delete
    fun deleteItemDeck(deck: Deck)
}