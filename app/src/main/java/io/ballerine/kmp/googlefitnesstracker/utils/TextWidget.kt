package io.ballerine.kmp.googlefitnesstracker.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ballerine.kmp.googlefitnesstracker.data.DailySteps
import io.ballerine.kmp.googlefitnesstracker.ui.theme.ColorPrimary
import io.ballerine.kmp.googlefitnesstracker.ui.theme.GRADIENT_2
import io.ballerine.kmp.googlefitnesstracker.ui.theme.LIGHT_PRIMARY_TEXT_COLOR

@Composable
fun SmallPrimaryTextStyle(text: String, textAlign: TextAlign = TextAlign.Start) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
        color = ColorPrimary,
        textAlign = textAlign
    )
}


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

@Composable
fun TextWidgetBar(data: DailySteps) {
    Column(
        modifier = Modifier
            .padding(bottom = 5.dp)
            .width(35.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
        ) {
            SmallPrimaryTextStyle(text = data.day)
        }
    }
}