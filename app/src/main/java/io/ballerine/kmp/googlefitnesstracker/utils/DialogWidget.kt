package io.ballerine.kmp.googlefitnesstracker.utils

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.ballerine.kmp.googlefitnesstracker.ui.theme.NEGATIVE_BUTTON_COLOR

@Composable
fun CustomDialog(openDialogCustom: MutableState<Boolean>) {
    Dialog(onDismissRequest = { openDialogCustom.value = false }) {
        SignOutDialog(onCancel = {}, onSignOut = {})
    }
}

//Layout
@Composable
fun SignOutDialog(onCancel: () -> Unit, onSignOut: () -> Unit) {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        Dialog(onDismissRequest = {}) {

            Card(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp)
            ) {

                Column(modifier = Modifier.padding(16.dp)) {

                    NormalBlackTextStyle18sp(
                        text = "Sign Out"
                    )

                    Spacer16Dp()

                    // Buttons
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        NormalTextButton(
                            text = "Cancel",
                            onClick = { onCancel() })
                        Spacer(modifier = Modifier.width(4.dp))
                        NormalTextButton(
                            text = "SignOut",
                            onClick = { onSignOut() },
                            textColor = NEGATIVE_BUTTON_COLOR
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(name = "Custom Dialog")
@Composable
fun MyDialogUIPreview() {
    SignOutDialog(onCancel = {}, onSignOut = {})
}