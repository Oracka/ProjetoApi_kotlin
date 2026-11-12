package com.yuri.personagens.userinterface

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yuri.personagens.data.AppDatabase
import com.yuri.personagens.data.Repository
import com.yuri.personagens.data.SessionManager
import com.yuri.personagens.network.RetrofitInstance
import com.yuri.personagens.ui.theme.PersonagensTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getInstance(applicationContext)
        val repository = Repository(
            usuarioDao = db.usuarioDao(),
            favoritoDao = db.favoritoDao(),
            api = RetrofitInstance.api,
            sessionManager = SessionManager(applicationContext)
        )
        setContent {
            PersonagensTheme {
            AppNavigation(repository)
        } // tema principal do app
        }
    }
}