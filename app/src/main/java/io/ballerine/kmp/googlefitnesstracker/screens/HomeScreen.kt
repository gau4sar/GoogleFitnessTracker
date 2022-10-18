package io.ballerine.kmp.googlefitnesstracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import io.ballerine.kmp.googlefitnesstracker.R
import io.ballerine.kmp.googlefitnesstracker.ui.theme.ColorPrimary
import io.ballerine.kmp.googlefitnesstracker.ui.theme.GRADIENT_2
import io.ballerine.kmp.googlefitnesstracker.ui.theme.LIGHT_PRIMARY_TEXT_COLOR
import io.ballerine.kmp.googlefitnesstracker.ui.theme.STROKE_BACKGROUND_COLOR
import io.ballerine.kmp.googlefitnesstracker.utils.*
import io.ballerine.kmp.googlefitnesstracker.viewmodel.SharedViewModel

@Composable
fun HomeScreen(
    onGoogleSignOutClick: () -> Unit,
    sharedViewModel: SharedViewModel
) {


    val localDensity = LocalDensity.current
    var descriptionHeight by remember {
        mutableStateOf(0.dp)
    }

    val toolbarLayoutId = "toolbarLayoutId"
    val progressLayoutId = "progressLayoutId"
    val caloriesBurnLayoutId = "caloriesBurnLayoutId"
    val graphLayoutId = "graphLayoutId"

    val constraints = ConstraintSet {

        val topPercent60 = createGuidelineFromTop(0.55f)

        val toolbarLayout = createRefFor(toolbarLayoutId)
        val progressLayout = createRefFor(progressLayoutId)
        val caloriesBurnLayout = createRefFor(caloriesBurnLayoutId)
        val graphLayout = createRefFor(graphLayoutId)

        constrain(toolbarLayout) {
            end.linkTo(parent.end)
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            width = Dimension.fillToConstraints
        }
        constrain(progressLayout) {
            end.linkTo(parent.end)
            start.linkTo(parent.start)
            top.linkTo(toolbarLayout.bottom)
            bottom.linkTo(topPercent60)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        }
        constrain(caloriesBurnLayout) {
            end.linkTo(parent.end)
            start.linkTo(parent.start)
            top.linkTo(progressLayout.bottom)
            width = Dimension.fillToConstraints
        }
        constrain(graphLayout) {
            end.linkTo(parent.end)
            start.linkTo(parent.start)
            bottom.linkTo(parent.bottom)
            top.linkTo(caloriesBurnLayout.bottom)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        }
    }

    ConstraintLayout(
        constraintSet = constraints, modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        ColorPrimary,
                        GRADIENT_2
                    )
                )
            )
    ) {

        Column(modifier = Modifier.layoutId(toolbarLayoutId)) {

            PrimaryToolBar(onGoogleSignOut = {
                onGoogleSignOutClick()
            })
        }

        //Progress bar for steps
        Box(
            modifier = Modifier
                .layoutId(progressLayoutId)
                .padding(start = 40.dp, end = 40.dp, top = 40.dp)
            /*.background(Color.Cyan)*/,
            contentAlignment = Alignment.Center
        ) {

            ComposeCircularProgressBar(
                percentage = sharedViewModel.stepsMutableState.value.toFloat() / 5000,
                fillColor = Color.White,
                backgroundColor = STROKE_BACKGROUND_COLOR,
                strokeWidth = 14.dp
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_foot_steps),
                    contentDescription = null,
                    tint = LIGHT_PRIMARY_TEXT_COLOR,
                    modifier = Modifier
                        .size(48.dp)
                )

                Spacer16Dp()

                BigWhiteTextStyle(sharedViewModel.stepsMutableState.value)
            }
        }

        //Calories - Speed - Distance
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .layoutId(caloriesBurnLayoutId)
                /*.background(Color.Yellow)*/
                .onGloballyPositioned { coordinates ->
                    descriptionHeight = with(localDensity) { coordinates.size.height.toDp() }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                MediumPrimaryTextStyle(text = stringResource(R.string.calories))

                BigWhiteTextStyle(
                    text = (sharedViewModel.stepsMutableState.value.toFloat() * 0.04
                            ).formatTo1Decimal()
                )

                MediumPrimaryTextStyle(text = stringResource(R.string.kcal))
            }

            SpacerLightPrimaryDp(descriptionHeight)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                MediumPrimaryTextStyle(text = stringResource(R.string.speed_avg))

                BigWhiteTextStyle(
                    text = (sharedViewModel.speedOfASessionMutableState.value.toFloat() * 3.6).formatTo1Decimal()
                )

                MediumPrimaryTextStyle(text = stringResource(R.string.km_h))
            }

            SpacerLightPrimaryDp(descriptionHeight)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                MediumPrimaryTextStyle(text = stringResource(R.string.distance))

                BigWhiteTextStyle(
                    text = (sharedViewModel.distanceOfASessionMutableState.value.toFloat() * 0.001).formatTo1Decimal()
                )

                MediumPrimaryTextStyle(text = stringResource(R.string.km))
            }
        }

        //Graph
        Column(
            modifier = Modifier
                .layoutId(graphLayoutId)
                .padding(top = 24.dp)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color.White)
                .padding(20.dp)
        ) {
            Chart(data = sharedViewModel.dailySteps, max_value = 10000,stepsMutableState=sharedViewModel.stepsMutableState)
        }
    }
}