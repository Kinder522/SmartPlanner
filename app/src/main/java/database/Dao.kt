package database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import models.Task


@Dao
interface Dao {

    @Insert
    fun inserttask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM Tasks ")
    fun getalltask(): Flow<List<Task>>



}