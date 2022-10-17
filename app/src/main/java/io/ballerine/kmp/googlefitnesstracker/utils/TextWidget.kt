package io.ballerine.kmp.googlefitnesstracker.utils

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import io.ballerine.kmp.googlefitnesstracker.ui.theme.GRADIENT_2
import io.ballerine.kmp.googlefitnesstracker.ui.theme.LIGHT_PRIMARY_TEXT_COLOR


@Composable
fun MediumPrimaryTextStyle(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = LIGHT_PRIMARY_TEXT_COLOR
    )
}

@Composable
fun NormalPrimaryTextStyle(text: String) {
    Text(
        text = text,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = GRADIENT_2
    )
}

@Composable
fun NormalWhiteTextStyle(text: String, textAlign: TextAlign = TextAlign.Start) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        textAlign = textAlign
    )
}

@Composable
fun NormalBlackTextStyle(
    text: String,
    textAlign: TextAlign = TextAlign.Start,
    textColor: Color = Color.Black
) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = textColor,
        textAlign = textAlign
    )
}

@Composable
fun NormalWhiteTextStyle18sp(text: String, textAlign: TextAlign = TextAlign.Start) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        textAlign = textAlign
    )
}

@Composable
fun NormalBlackTextStyle18sp(text: String, textAlign: TextAlign = TextAlign.Start) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.Black,
        textAlign = textAlign
    )
}

@Composable
fun NormalWhiteTextStyle2(text: String) {
    Text(
        text = text,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White
    )
}

@Composable
fun BigWhiteTextStyle(text: String, textAlign: TextAlign = TextAlign.Start) {
    Text(
        text = text,
        fontSize = 40.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        textAlign = textAlign
    )
}