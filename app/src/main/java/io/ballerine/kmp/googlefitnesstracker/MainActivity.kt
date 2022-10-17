package io.ballerine.kmp.googlefitnesstracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.result.DataReadResponse
import io.ballerine.kmp.googlefitnesstracker.screens.GoogleSignInScreen
import io.ballerine.kmp.googlefitnesstracker.screens.HomeScreen
import io.ballerine.kmp.googlefitnesstracker.ui.theme.GoogleFitnessTrackerTheme
import io.ballerine.kmp.googlefitnesstracker.ui.theme.STATUS_BAR_COLOR
import io.ballerine.kmp.googlefitnesstracker.utils.SignOutDialog
import io.ballerine.kmp.googlefitnesstracker.utils.showToast
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {

    private val googleFitRequestCode = 1001

    private var stepsMutableState = mutableStateOf("")

    private var isGoogleSignInProgress = mutableStateOf(false)

    private var isShowSignOutDialog = mutableStateOf(false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val systemUiController = rememberSystemUiController()

            systemUiController.setSystemBarsColor(
                color = STATUS_BAR_COLOR
            )


            GoogleFitnessTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    if (!isGoogleSignedIn.value) {
                        GoogleSignInScreen(
                            isGoogleSignInProgress = isGoogleSignInProgress,
                            onGoogleSignInClick = {
                                isGoogleSignInProgress.value = true
                                setUpGoogleSignIn()
                            })
                    } else {
                        HomeScreen(stepsMutableState = stepsMutableState, onGoogleSignOutClick = {
                            /*onGoogleSignOut()*/

                            isShowSignOutDialog.value = true
                        })
                    }

                    if (isShowSignOutDialog.value) {
                        SignOutDialog(onCancel = {
                            isShowSignOutDialog.value = false
                        }) {
                            isShowSignOutDialog.value = false
                            onGoogleSignOut()
                        }
                    }
                }
            }
        }

        setUpGoogleSignIn()
    }

    private fun onGoogleSignOut() {
        GoogleSignIn.getClient(applicationContext, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()
            .addOnSuccessListener {
                isGoogleSignedIn.value = false
            }
    }

    val isGoogleSignedIn = mutableStateOf(false)
    private fun setUpGoogleSignIn() {

        val fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
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
            isGoogleSignInProgress.value = false
            isGoogleSignedIn.value = true
            displayStepDataForToday()
        }
    }

    private fun displayStepDataForToday() {

        val readRequest = queryFitnessData()
        GoogleSignIn.getLastSignedInAccount(this)?.let {

            Log.d("getLastSignedInAccount", "${it.displayName} ${it.email} ${it.photoUrl}")

            Fitness.getHistoryClient(this, it)
                .readData(readRequest)
                .addOnSuccessListener { dataReadResponse ->
                    printData(dataReadResponse)
                }
                .addOnFailureListener { e ->
                    showToast("There was a problem reading the data. $e")
                    Log.e("tag", "There was a problem reading the data.", e)
                }
        }
    }


    private fun queryFitnessData(): DataReadRequest {
        val calEndTime = Calendar.getInstance()
        val calendar = Calendar.getInstance()
        val formatDateTime = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(formatDateTime, Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.MILLISECOND] = 0
        calendar[Calendar.SECOND] = 0

        return DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .bucketByTime(1, TimeUnit.HOURS)
            .setTimeRange(calendar.timeInMillis, calEndTime.timeInMillis, TimeUnit.MILLISECONDS)
            .build()
    }

    private fun printData(dataReadResult: DataReadResponse) {
        if (dataReadResult.buckets.size > 0) {
            for (bucket in dataReadResult.buckets) {
                val dataSets = bucket.dataSets
                for (dataSet in dataSets) {
                    dumpDataSet(dataSet)
                }
            }
        } else if (dataReadResult.dataSets.size > 0) {
            for (dataSet in dataReadResult.dataSets) {
                dumpDataSet(dataSet)
            }
        }
    }

    private fun dumpDataSet(dataSet: DataSet) {
        if (dataSet.dataPoints.size == 0) {
            stepsMutableState.value = "0"
        }
        for (dp in dataSet.dataPoints) {
            for (field in dp.dataType.fields) {
                stepsMutableState.value = dp.getValue(field).toString()
            }
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
            isGoogleSignInProgress.value = false
        } else {
            Log.e("onActivityResult", "Failed")
            showToast("Failed!")
            isGoogleSignInProgress.value = false
        }
    }
}