package com.example.diosroom

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface DaoUsuario {

    @Query("SELECT * FROM usuario")
    suspend fun  obtenerUsuarios(): MutableList<Usuario>

    @Insert
    suspend fun agregarUsuario(usuario: Usuario)

    @Query("UPDATE usuario set pais=:pais WHERE usuario=:usuario")
    suspend fun actualizarUsuario(usuario: String, pais: String)

    @Query("DELETE FROM usuario WHERE usuario=:usuario")
    suspend fun borrarUsuario(usuario: String)
}