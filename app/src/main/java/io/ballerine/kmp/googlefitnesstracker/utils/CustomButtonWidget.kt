package io.ballerine.kmp.googlefitnesstracker.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.ballerine.kmp.googlefitnesstracker.R
import io.ballerine.kmp.googlefitnesstracker.ui.theme.ColorPrimary

@Composable
fun GoogleSignInButton(inProgress: Boolean, onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            contentColor = Color.White
        ),
    ) {
        if (inProgress) {
            CircularProgressIndicator(
                modifier = Modifier.size(28.dp),
                color = ColorPrimary
            )
        } else {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.ic_google_logo),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(end = 24.dp)
                            .size(24.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NormalBlackTextStyle18sp(
                        text = stringResource(R.string.sign_in_google)
                    )
                }
            }
        }
    }
}