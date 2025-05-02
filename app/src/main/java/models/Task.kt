package models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description:String,
    @ColumnInfo(name = "datetime")
    val datetime:String,
    @ColumnInfo(name = "isDone")
    val isDone: Boolean = false
) : Parcelable
