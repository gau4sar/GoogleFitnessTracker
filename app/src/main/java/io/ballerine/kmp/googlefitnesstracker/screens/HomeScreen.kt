package io.ballerine.kmp.googlefitnesstracker.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(onGoogleSignInClick: () -> Unit, stepsMutableState: MutableState<String>) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "${stepsMutableState.value}/1000 steps", fontSize = 22.sp)

        Spacer(modifier = Modifier.padding(36.dp))

        Button(onClick = { onGoogleSignInClick() }) {
            Text(text = "Google sign in")
        }
    }
}