package com.github.adizcode.lookout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.adizcode.lookout.model.ClassroomViewModel
import com.github.adizcode.lookout.ui.composable.App
import com.github.adizcode.lookout.ui.theme.LookoutTheme

// 1. Setup the navigation flow - Done
// 2. Setup Retrofit and integrate - Done
// 3. Refactor composables for reusability and side-effects - Done
// 4. Improve UI from tutorials and blogs
// 5. Setup theming
// 6. Add animations
class MainActivity : ComponentActivity() {
    private val classroomViewModel: ClassroomViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LookoutTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    App(viewModel = classroomViewModel)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LookoutTheme {
        App(ClassroomViewModel())
    }
}