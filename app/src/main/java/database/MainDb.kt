package database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import models.Task

@Database (entities = [Task::class], version = 2)
abstract class MainDb :RoomDatabase(){
    abstract fun getDao(): Dao


    companion object{
        fun getDb(context: Context):MainDb{
            return Room.databaseBuilder(
                context.applicationContext,
                MainDb::class.java,
                "Task.db"
            ).build()
        }
    }

}