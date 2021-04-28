package com.jbvm.currency.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.jbvm.currency.data.entities.Symbol
import io.reactivex.Single

@Dao
interface SymbolDao {
    @Insert(onConflict = REPLACE)
    fun save(symbol: Symbol)

    @Insert(onConflict = REPLACE)
    fun saveAll(symbols: List<Symbol>)

    @Query("SELECT * FROM Symbol WHERE id = :symbolId")
    fun load(symbolId: String): Single<Symbol>

    @Query("SELECT * FROM Symbol")
    fun loadAll(): Single<List<Symbol>>
}