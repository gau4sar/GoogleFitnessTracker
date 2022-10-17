package io.ballerine.kmp.googlefitnesstracker.utils

import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun NormalTextButton(text: String, onClick: () -> Unit, textColor: Color = Color.Black) {

    TextButton(onClick = onClick) {
        NormalBlackTextStyle(text = text, textColor = textColor)
    }
}