package com.example.donow.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(DateConvertor::class)
@Database(entities = [TasksEntity::class, SubTaskEntity::class], version = 1, exportSchema = false)
abstract class TasksDatabase :RoomDatabase(){
    abstract fun daoInterface() : DaoInterFace

    companion object{
        private var tasksDatabase : TasksDatabase? = null

        fun getInstance(context : Context) : TasksDatabase {
            if(tasksDatabase == null){
                tasksDatabase = Room.databaseBuilder(context,
                    TasksDatabase::class.java,
                    "TasksDatabase")
                    .allowMainThreadQueries()
                    .build()
            }
            return tasksDatabase!!
        }
    }
}