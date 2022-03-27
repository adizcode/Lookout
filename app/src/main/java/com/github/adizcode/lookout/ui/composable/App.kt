package com.github.adizcode.lookout.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.adizcode.lookout.model.ClassroomViewModel
import com.github.adizcode.lookout.model.enums.Screen

@Composable
fun App(viewModel: ClassroomViewModel) {

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        if (viewModel.isLoading)
            Loader()
        else
            CurrentScreen(viewModel)
    }
}

@Composable
fun CurrentScreen(viewModel: ClassroomViewModel) {
    when (viewModel.screen) {
        Screen.CHOOSE_PROFILE -> ProfileScreen(viewModel)
        Screen.LOGIN -> LoginScreen(viewModel)
        Screen.HOME -> HomeScreen(viewModel)
    }
}

@Composable
fun Loader() {
    CircularProgressIndicator()
}