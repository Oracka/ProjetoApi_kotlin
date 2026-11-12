package com.yuri.personagens.userinterface


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.yuri.personagens.viewmodel.FavoritosViewModel

@Composable
fun FavoritosScreen(
    viewModel: FavoritosViewModel,
    onFavoritoClick: (Int) -> Unit,
    onVoltar: () -> Unit
) {
    val favoritos by viewModel.favoritos.collectAsStateWithLifecycle()
    val state by viewModel.uiState.collectAsStateWithLifecycle() // !

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(16.dp)) {
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Favoritos de ${state.nomeUsuario}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.surface,
            )

            Button(onClick = onVoltar) { Text("Voltar") }
        }
        Spacer(Modifier.height(16.dp))
        if (favoritos.isEmpty()) {
            Text("Nenhum favorito ainda.")
        } else {
            LazyColumn {
                items(favoritos) { fav ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onFavoritoClick(fav.characterId) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = fav.imagem,
                            contentDescription = fav.nome,
                            modifier = Modifier.size(80.dp))
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(fav.nome, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.surface)
                            Text(text = "ID: ${fav.characterId}", color = MaterialTheme.colorScheme.surface)
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
