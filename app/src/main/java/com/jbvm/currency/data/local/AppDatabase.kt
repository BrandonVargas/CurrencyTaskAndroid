package com.jbvm.currency.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jbvm.currency.data.entities.Symbol

@Database(entities = [Symbol::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun symbolDao(): SymbolDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "currency")
                .fallbackToDestructiveMigration()
                .build()
    }
}