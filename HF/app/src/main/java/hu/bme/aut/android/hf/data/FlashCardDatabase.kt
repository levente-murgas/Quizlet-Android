package hu.bme.aut.android.hf.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Deck::class, FlashCard::class], version = 3)
abstract class FlashCardDatabase : RoomDatabase() {
    abstract fun allDao(): AllDao

    companion object {
        fun getDatabase(applicationContext: Context): FlashCardDatabase {
            return Room.databaseBuilder(
                applicationContext,
                FlashCardDatabase::class.java,
                "flashcards"
            ).fallbackToDestructiveMigration().build();
        }
    }
}