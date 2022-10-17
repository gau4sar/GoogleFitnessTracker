package io.ballerine.kmp.googlefitnesstracker.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.ballerine.kmp.googlefitnesstracker.R
import io.ballerine.kmp.googlefitnesstracker.ui.theme.GRADIENT_2
import io.ballerine.kmp.googlefitnesstracker.ui.theme.STATUS_BAR_COLOR
import io.ballerine.kmp.googlefitnesstracker.ui.theme.VERY_LIGHT_PRIMARY_TEXT_COLOR

@Composable
fun PrimaryToolBar(onGoogleSignOut: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(STATUS_BAR_COLOR)
            .padding(12.dp)
            .padding(start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
/*

        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_menu_24),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .clickable {
                    onGoogleSignIn()
                }
        )
*/

        Row(
            modifier = Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.Start
        ) {

            NormalWhiteTextStyle2(text = "Fitness Tracker")
        }

        Row {

            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_person_24),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.clickable { onGoogleSignOut() }
            )

            /*Spacer16Dp()*/

            /*Icon(
                painter = painterResource(id = R.drawable.ic_baseline_notifications_none_24),
                contentDescription = null,
                tint = Color.White
            )*/
        }
    }
}