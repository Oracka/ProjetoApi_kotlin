package com.yuri.personagens.userinterface

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.yuri.personagens.data.Repository
import com.yuri.personagens.viewmodel.CadastroViewModel
import com.yuri.personagens.viewmodel.FavoritosViewModel
import com.yuri.personagens.viewmodel.HomeViewModel
import com.yuri.personagens.viewmodel.LoginViewModel
import kotlinx.serialization.Serializable

@Serializable object RotaLogin
@Serializable object RotaCadastro
@Serializable data class RotaHome(val personagemId: Int? = null)
@Serializable object RotaFavoritos

@Composable
fun AppNavigation(repository: Repository) {
    val navController = rememberNavController()
    val startDestination = if (repository.estaLogado()) RotaHome() else RotaLogin

    NavHost(navController = navController, startDestination = startDestination) {

        composable<RotaLogin> {
            val vm: LoginViewModel = viewModel { LoginViewModel(repository) }
            LoginScreen(
                viewModel = vm,
                onNavigateToCadastro = { navController.navigate(RotaCadastro) },
                onLoginSuccess = {
                    navController.navigate(RotaHome()) {
                        popUpTo(RotaLogin) { inclusive = true }
                    }
                }
            )
        }

        composable<RotaCadastro> {
            val vm: CadastroViewModel = viewModel { CadastroViewModel(repository) }
            CadastroScreen(
                viewModel = vm,
                onCadastroConcluido = { navController.popBackStack() },
                onVoltarLogin = { navController.popBackStack() }
            )
        }

        composable<RotaHome> { backStackEntry ->
            val rota = backStackEntry.toRoute<RotaHome>()
            val vm: HomeViewModel = viewModel { HomeViewModel(repository) }
            HomeScreen(
                viewModel = vm,
                personagemIdInicial = rota.personagemId,
                onNavigateToFavoritos = { navController.navigate(RotaFavoritos) },
                onSair = {
                    vm.toggleFavorito()
                    navController.navigate(RotaLogin) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable<RotaFavoritos> {
            val vm: FavoritosViewModel = viewModel { FavoritosViewModel(repository) }
            FavoritosScreen(
                viewModel = vm,
                onFavoritoClick = { id ->
                    navController.navigate(RotaHome(personagemId = id)) {
                        popUpTo(RotaHome()) { inclusive = true }
                    }
                },
                onVoltar = { navController.popBackStack() }
            )
        }
    }
}