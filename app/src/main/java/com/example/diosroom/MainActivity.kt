package com.example.diosroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.diosroom.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), AdaptadorListener {

    lateinit var binding: ActivityMainBinding

    var listaUsuario: MutableList<Usuario> = mutableListOf()

    lateinit var adaptador: AdaptadorUsuarios

    lateinit var room: DBPrueba

    lateinit var usuario: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUsuarios.layoutManager = LinearLayoutManager(this)

        room = Room.databaseBuilder(this, DBPrueba::class.java, "dbPruebas").build()

         obtenerUsuarios(room)

        binding.btnAddUpdate.setOnClickListener {
            if(binding.etUsuario.text.isNullOrEmpty() || binding.etPais.text.isNullOrEmpty()){
                Toast.makeText(this, "DEBES LLENAR TODO", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.btnAddUpdate.text.equals("agregar")){
                usuario = Usuario(
                    binding.etUsuario.text.toString().trim(),
                    binding.etPais.text.toString().trim()

                )

                agregarUsuario(room, usuario)
            }else if (binding.btnAddUpdate.text.equals("actualizar")){
                usuario.pais = binding.etPais.text.toString().trim()

                actualizarUsuario(room, usuario)
            }
        }
    }

    fun obtenerUsuarios(room: DBPrueba){
        lifecycleScope.launch {
            listaUsuario = room.daoUsuario().obtenerUsuarios()
            adaptador = AdaptadorUsuarios(listaUsuario, this@MainActivity)
            binding.rvUsuarios.adapter = adaptador
        }
    }

    fun agregarUsuario(room: DBPrueba, usuario: Usuario){
        lifecycleScope.launch {
            room.daoUsuario().agregarUsuario(usuario)
            obtenerUsuarios(room)
            limpiarCampos()
        }
    }

    fun actualizarUsuario(room: DBPrueba, usuario: Usuario){
        lifecycleScope.launch {
            room.daoUsuario().actualizarUsuario(usuario.usuario, usuario.pais)
            obtenerUsuarios(room)
            limpiarCampos()
        }

    }

    fun limpiarCampos(){
        usuario.usuario = ""
        usuario.pais = ""
        binding.etUsuario.setText("")
        binding.etPais.setText("")

        if(binding.btnAddUpdate.text.equals("actualizar")){
            binding.btnAddUpdate.text.equals("agregar")
            binding.etUsuario.isEnabled = true
        }
    }


    override fun onEditItemClick(usuario: Usuario) {
       binding.btnAddUpdate.setText("actualizar")
        binding.etUsuario.isEnabled = false
        this.usuario = usuario
        binding.etUsuario.setText(this.usuario.usuario)
        binding.etPais.setText(this.usuario.pais)
    }

    override fun onDeleteItemClick(usuario: Usuario) {
        lifecycleScope.launch {
            room.daoUsuario().borrarUsuario(usuario.usuario)
            adaptador.notifyDataSetChanged()
            obtenerUsuarios(room)
        }
    }
}