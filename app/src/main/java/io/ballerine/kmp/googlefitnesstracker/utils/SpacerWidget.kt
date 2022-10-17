package io.ballerine.kmp.googlefitnesstracker.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.ballerine.kmp.googlefitnesstracker.ui.theme.LIGHT_PRIMARY_TEXT_COLOR

@Composable
fun Spacer16Dp() {

    Spacer(modifier = Modifier.size(16.dp))
}

@Composable
fun Spacer32Dp() {

    Spacer(modifier = Modifier.size(32.dp))
}

@Composable
fun SpacerLightPrimaryDp(dp: Dp) {

    Spacer(
        modifier = Modifier
            .height(dp)
            .width(6.dp)
            .padding(vertical = 28.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(LIGHT_PRIMARY_TEXT_COLOR)
    )
}