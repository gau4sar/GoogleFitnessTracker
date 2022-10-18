package io.ballerine.kmp.googlefitnesstracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import io.ballerine.kmp.googlefitnesstracker.R
import io.ballerine.kmp.googlefitnesstracker.ui.theme.ColorPrimary
import io.ballerine.kmp.googlefitnesstracker.ui.theme.GRADIENT_2
import io.ballerine.kmp.googlefitnesstracker.utils.*

@Composable
fun GoogleSignInScreen(
    onGoogleSignInClick: () -> Unit,
    isGoogleSignInProgress: MutableState<Boolean>
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        ColorPrimary,
                        GRADIENT_2
                    )
                )
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Spacer32Dp()

        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.walkingjson))
        LottieAnimation(
            composition = composition,
            modifier = Modifier
                .fillMaxWidth()
                .size(160.dp),
            iterations = LottieConstants.IterateForever
        )

        BigWhiteTextStyle(text = stringResource(R.string.welcome), textAlign = TextAlign.Center)

        Spacer32Dp()

        NormalWhiteTextStyle18sp(
            text = stringResource(R.string.track_your_steps),
            textAlign = TextAlign.Center
        )

        Spacer32Dp()

        GoogleSignInButton(isGoogleSignInProgress.value) {
            onGoogleSignInClick()
        }
    }
}