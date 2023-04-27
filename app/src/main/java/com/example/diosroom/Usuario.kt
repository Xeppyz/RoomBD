package com.example.diosroom

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "usuario")
data class Usuario (
    @PrimaryKey var usuario: String,
    @ColumnInfo(name ="pais") var pais:String
        )
