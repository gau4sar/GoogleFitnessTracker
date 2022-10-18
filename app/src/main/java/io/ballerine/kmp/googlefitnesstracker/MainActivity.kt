package io.ballerine.kmp.googlefitnesstracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import io.ballerine.kmp.googlefitnesstracker.screens.GoogleSignInScreen
import io.ballerine.kmp.googlefitnesstracker.screens.HomeScreen
import io.ballerine.kmp.googlefitnesstracker.ui.theme.GoogleFitnessTrackerTheme
import io.ballerine.kmp.googlefitnesstracker.ui.theme.STATUS_BAR_COLOR
import io.ballerine.kmp.googlefitnesstracker.utils.ErrorMessageDialog
import io.ballerine.kmp.googlefitnesstracker.utils.SignOutDialog
import io.ballerine.kmp.googlefitnesstracker.utils.showToast
import io.ballerine.kmp.googlefitnesstracker.viewmodel.SharedViewModel


class MainActivity : ComponentActivity() {

    private val googleFitRequestCode = 1001

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val systemUiController = rememberSystemUiController()

            systemUiController.setSystemBarsColor(color = STATUS_BAR_COLOR)

            GoogleFitnessTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    if (!isGoogleSignedIn.value) {
                        GoogleSignInScreen(
                            isGoogleSignInProgress = sharedViewModel.isGoogleSignInProgress,
                            onGoogleSignInClick = {
                                sharedViewModel.isGoogleSignInProgress.value = true
                                setUpGoogleSignIn()
                            })
                    } else {
                        HomeScreen(
                            sharedViewModel = sharedViewModel,
                            onGoogleSignOutClick = {
                                sharedViewModel.isShowSignOutDialog.value = true
                            })
                    }

                    if (sharedViewModel.isShowSignOutDialog.value) {
                        SignOutDialog(onCancel = {
                            sharedViewModel.isShowSignOutDialog.value = false
                        }) {
                            sharedViewModel.isShowSignOutDialog.value = false
                            sharedViewModel.clearDate()
                            onGoogleSignOut()
                        }
                    }

                    if (sharedViewModel.errorMessages.isNotEmpty()) {
                        ErrorMessageDialog(
                            text = sharedViewModel.errorMessages.first(),
                            onDismiss = { sharedViewModel.errorMessages.removeFirst() })
                    }
                }
            }
        }

        setUpGoogleSignIn()
    }

    private val isGoogleSignedIn = mutableStateOf(false)
    private fun setUpGoogleSignIn() {

        val fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_ACTIVITY_SEGMENT, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_SPEED, FitnessOptions.ACCESS_READ)
            .build()

        if (!GoogleSignIn.hasPermissions(
                GoogleSignIn.getLastSignedInAccount(this),
                fitnessOptions
            )
        ) {
            isGoogleSignedIn.value = false
            GoogleSignIn.requestPermissions(
                this, // your activity instance
                googleFitRequestCode,
                GoogleSignIn.getLastSignedInAccount(this),
                fitnessOptions
            )
        } else {
            sharedViewModel.isGoogleSignInProgress.value = false
            isGoogleSignedIn.value = true
            displayStepDataForToday()
        }
    }

    private fun onGoogleSignOut() {
        GoogleSignIn.getClient(applicationContext, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()
            .addOnSuccessListener {
                isGoogleSignedIn.value = false
            }
    }


    private fun displayStepDataForToday() {
        GoogleSignIn.getLastSignedInAccount(this)?.let {

            val historyClient = Fitness.getHistoryClient(this, it)
            sharedViewModel.readRequests(historyClient)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == googleFitRequestCode) {
                isGoogleSignedIn.value = true
                displayStepDataForToday()
            } else {
                Log.e("onActivityResult", "Failed")
                showToast("Failed!")
            }
            sharedViewModel.isGoogleSignInProgress.value = false
        } else {
            Log.e("onActivityResult", "Failed")
            showToast("Failed!")
            sharedViewModel.isGoogleSignInProgress.value = false
        }
    }
}