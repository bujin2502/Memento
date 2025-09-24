package hr.foi.rampu.memento.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import java.util.Date
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import hr.foi.rampu.memento.converters.DateConverter

@Entity(
    "tasks",
    foreignKeys = [ForeignKey(
        entity = TaskCategory::class,
        parentColumns = ["id"],
        childColumns = ["category_id"],
        onDelete = ForeignKey.RESTRICT
    )]
)
data class Task(
    @PrimaryKey(autoGenerate = true) val id : Int,
    val name : String,
    @TypeConverters(DateConverter::class)
    @ColumnInfo(name = "due_date") val dueDate : Date,
    @ColumnInfo(name = "category_id", index = true) val categoryId: Int,
    val completed : Boolean
) {
    @Ignore
    lateinit var category : TaskCategory
}
