package com.jbvm.currency.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Symbol")
data class Symbol(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val abbreviation: String,
    val name: String
)
