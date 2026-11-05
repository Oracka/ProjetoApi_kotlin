package com.yuri.personagens.userinterface

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.yuri.personagens.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    personagemIdInicial: Int? = null,
    onNavigateToFavoritos: () -> Unit,
    onSair: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var termo by remember { mutableStateOf("") }

    LaunchedEffect(personagemIdInicial) {
        if (personagemIdInicial != null) {
            termo = personagemIdInicial.toString()
            viewModel.pesquisar(termo)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = onNavigateToFavoritos) { Text("Favoritos") }
            Button(onClick = onSair) { Text("Sair") }
        }
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = termo,
            onValueChange = { termo = it },
            label = { Text("Nome ou ID do personagem") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { viewModel.pesquisar(termo) },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Pesquisar") }

        Spacer(Modifier.height(16.dp))

        when {
            state.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            state.erro != null -> Text(state.erro!!, color = MaterialTheme.colorScheme.error)
            state.personagem != null -> {
                val p = state.personagem!!
                AsyncImage(model = p.image, contentDescription = p.name, modifier = Modifier.fillMaxWidth().height(200.dp))
                Spacer(Modifier.height(8.dp))
                Text("Nome: ${p.name}")
                Text("ID: ${p.id}")
                Text("Gênero: ${p.gender}")
                Text("Espécie: ${p.species}")
                Text("Localização: ${p.location.name}")
                Text("Status: ${p.status}")
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.toggleFavorito() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.favoritado)
                            MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(if (state.favoritado) "remover dos favoritos" else "Favoritar")
                }
            }
        }
    }
}

