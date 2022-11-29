package hu.bme.aut.android.hf.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards",
    foreignKeys = [ForeignKey(
        entity = Deck::class,
        childColumns = ["deck_id"],
        parentColumns = ["id"],
        onDelete = CASCADE,
        onUpdate = CASCADE
    )])
data class FlashCard(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "term") var term: String,
    @ColumnInfo(name = "definition") var definition: String,
    @ColumnInfo(name = "study_again") var studyAgain: Boolean,
    @ColumnInfo(name = "deck_id") var deck_id: Long? = null
)
