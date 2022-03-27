package com.github.adizcode.lookout.ui.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.adizcode.lookout.model.ClassroomViewModel
import com.github.adizcode.lookout.model.data.LoginCredentials
import com.github.adizcode.lookout.model.enums.Profile
import com.github.adizcode.lookout.model.enums.Screen

@Composable
fun ProfileScreen(viewModel: ClassroomViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Text("Choose Profile", style = MaterialTheme.typography.h4)
//        Spacer(Modifier.height(24.dp))
        Row(Modifier.padding(20.dp)) {
            ProfileButton(text = "Teacher", modifier = Modifier.weight(.5f)) {
                viewModel.goToProfileLogin(Profile.TEACHER)
            }
            ProfileButton(text = "Student", modifier = Modifier.weight(.5f)) {
                viewModel.goToProfileLogin(Profile.STUDENT)
            }
        }
    }
}

@Composable
private fun ProfileButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(1f)
            .padding(15.dp)
    ) {
        Text(text = text, fontSize = 18.sp)
    }
}

@Composable
fun LoginScreen(viewModel: ClassroomViewModel) {
    val (username, setUsername) = rememberSaveable { mutableStateOf("") }
    val (password, setPassword) = rememberSaveable { mutableStateOf("") }
    val (isPasswordVisible, setPasswordVisible) = rememberSaveable { mutableStateOf(false) }
    val (isUsernameInvalid, setUsernameInvalid) = rememberSaveable { mutableStateOf(false) }

    val profile = with(viewModel.profile.toString()) {
        this[0] + this.substring(1).lowercase()
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),

        ) {
        Text("$profile Login", style = MaterialTheme.typography.h6)
        OutlinedTextField(
            value = username,
            onValueChange = setUsername,
            label = { Text("Username") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = isUsernameInvalid || viewModel.loginUnsuccessful,
        )
        OutlinedTextField(
            value = password,
            onValueChange = setPassword,
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = viewModel.loginUnsuccessful,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image =
                    if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                val contentDesc = if (isPasswordVisible) "Hide password" else "Show password"

                IconButton(onClick = { setPasswordVisible(!isPasswordVisible) }) {
                    Icon(imageVector = image, contentDescription = contentDesc)
                }
            }
        )
        Button(onClick = {
            // Validate email
            val emailRegex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$".toRegex()
            setUsernameInvalid(!emailRegex.matches(username))

            if (!isUsernameInvalid && username.isNotBlank() && password.isNotBlank()) {
                // Make the login call if fields are not empty
                viewModel.loginUser(LoginCredentials(username, password))
            }
        }) {
            Text("Login")
        }
        Button(onClick = { viewModel.navigateToScreen(Screen.CHOOSE_PROFILE) }) {
            Text("Go Back")
        }
    }
}

@Composable
fun HomeScreen(viewModel: ClassroomViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(75.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (viewModel.profile) {
            Profile.TEACHER -> TeacherHome(viewModel)
            Profile.STUDENT -> StudentHome(viewModel)
            else -> throw Exception("No Profile Detected")
        }

        Button(onClick = {
            // Sign out should pop all screens except the first one and clear shared prefs
            viewModel.signOut()
        }
        ) {
            Text("Sign Out")
        }
    }
}