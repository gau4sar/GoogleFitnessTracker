package io.ballerine.kmp.googlefitnesstracker.utils


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import io.ballerine.kmp.googlefitnesstracker.R
import io.ballerine.kmp.googlefitnesstracker.data.DailySteps
import io.ballerine.kmp.googlefitnesstracker.ui.theme.GRADIENT_2
import io.ballerine.kmp.googlefitnesstracker.utils.Constant.GOAL_STEPS

@Composable
fun Chart(
    data: SnapshotStateList<DailySteps>,
    max_value: Int,
    stepsMutableState: MutableState<String>
) {

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize(),
        /*verticalArrangement = Arrangement.Bottom*/
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
            /*.background(Color.Red)*/,
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {

            Box(
                modifier = Modifier
                    .fillMaxHeight(),
                contentAlignment = Alignment.BottomCenter
            ) {
                // scale
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    NormalPrimaryTextStyle(text = max_value.toString())
                    Spacer(modifier = Modifier.fillMaxHeight())
                }

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    NormalPrimaryTextStyle(text = "Goal")
                    Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                }

            }

            /*Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .background(GRADIENT_2)
            )*/

            val progressLayoutId = "progressLayoutId"
            val barLayoutId = "barLayoutId"
            val bottomLayoutId = "bottomLayoutId"

            val constraints = ConstraintSet {

                val topPercent60 = createGuidelineFromTop(0.5f)

                val progressLayout = createRefFor(progressLayoutId)
                val barLayout = createRefFor(barLayoutId)
                val bottomLayout = createRefFor(bottomLayoutId)

                constrain(progressLayout) {
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    top.linkTo(topPercent60)
                    bottom.linkTo(topPercent60)
                    width = Dimension.fillToConstraints
                }

                constrain(barLayout) {
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }

                constrain(bottomLayout) {
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    top.linkTo(barLayout.bottom)
                    width = Dimension.fillToConstraints
                }
            }

            ConstraintLayout(
                constraintSet = constraints, modifier = Modifier
                    .fillMaxSize()
                /*.padding(start = 16.dp)*/
            ) {

                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .layoutId(progressLayoutId)
                        .background(Color.Cyan),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Canvas(
                        Modifier
                            .fillMaxWidth()
                            .background(Color.Red)
                    ) {

                        drawLine(
                            color = GRADIENT_2,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            pathEffect = pathEffect,
                            strokeWidth = 5f
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .layoutId(barLayoutId)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = if(data.size<4) Arrangement.Start else Arrangement.SpaceEvenly
                    ) {

                        data.forEach {

                            BarWidget(context, it)
                        }
                        BarWidget(
                            context,
                            DailySteps(
                                stringResource(R.string.today),
                                stepsMutableState.value.toFloat() / GOAL_STEPS
                            )
                        )
                    }
                }


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .layoutId(bottomLayoutId)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                        /*.background(Color.Cyan)*/,
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = if(data.size<4) Arrangement.Start else Arrangement.SpaceEvenly
                    ) {
                        data.forEach {

                            TextWidgetBar(it)
                        }
                        TextWidgetBar(
                            DailySteps(
                                stringResource(R.string.today),
                                stepsMutableState.value.toFloat() / GOAL_STEPS
                            )
                        )
                    }
                }
            }
        }

        /*Row(
            modifier = Modifier
                .fillMaxWidth().background(Color.Transparent),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(50.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                // scale
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    NormalPrimaryTextStyle(text = max_value.toString())
                    Spacer(modifier = Modifier.fillMaxHeight())
                }

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    NormalPrimaryTextStyle(text = "Goal")
                    Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                }

            }

            *//*Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .background(GRADIENT_2)
            )*//*

            val progressLayoutId = "progressLayoutId"
            val barLayoutId = "barLayoutId"

            val constraints = ConstraintSet {

                val topPercent60 = createGuidelineFromTop(0.5f)

                val progressLayout = createRefFor(progressLayoutId)
                val barLayout = createRefFor(barLayoutId)

                constrain(progressLayout) {
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    top.linkTo(topPercent60)
                    bottom.linkTo(topPercent60)
                    width = Dimension.fillToConstraints
                }

                constrain(barLayout) {
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
            }

            ConstraintLayout(
                constraintSet = constraints, modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .layoutId(barLayoutId)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // graph
                        data.forEach {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .padding(end = 26.dp, bottom = 5.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .width(8.dp)
                                        .fillMaxHeight(it.steps)
                                        *//*.background(GRADIENT_2)
                                        .clickable {
                                            Toast
                                                .makeText(
                                                    context,
                                                    it.steps.toString(),
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }*//*
                                ) {

                                }

                                Text(
                                    modifier = Modifier.width(20.dp),
                                    text = it.date.toString(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }*/
    }
}

@Composable
fun BarWidget(context: Context, data: DailySteps) {

    Column(
        modifier = Modifier
            .padding(bottom = 5.dp)
            .width(35.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .width(8.dp)
                .fillMaxHeight(data.getSteps())
                .background(GRADIENT_2)
                .clickable {
                    Toast
                        .makeText(
                            context,
                            (data.steps * GOAL_STEPS)
                                .toInt()
                                .toString(),
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
        )
    }
}