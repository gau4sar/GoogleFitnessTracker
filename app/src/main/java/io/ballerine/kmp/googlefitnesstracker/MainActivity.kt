package io.ballerine.kmp.googlefitnesstracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataType
import io.ballerine.kmp.googlefitnesstracker.screens.HomeScreen
import io.ballerine.kmp.googlefitnesstracker.ui.theme.GoogleFitnessTrackerTheme
import java.text.DateFormat
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {

    private val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoogleFitnessTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HomeScreen(onGoogleSignInClick = {
                        setUpGoogleSignIn()
                    })
                }
            }
        }

        setUpGoogleSignIn()
    }

    private fun setUpGoogleSignIn() {

        val fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
            .build()

        if (!GoogleSignIn.hasPermissions(
                GoogleSignIn.getLastSignedInAccount(this),
                fitnessOptions
            )
        ) {
            GoogleSignIn.requestPermissions(
                this, // your activity instance
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                GoogleSignIn.getLastSignedInAccount(this),
                fitnessOptions
            )
        } else {
            displayStepDataForToday()
        }
    }

    private fun displayStepDataForToday() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

        val result =
            Fitness.HistoryApi.readDailyTotal(mGoogleApiClient, DataType.TYPE_STEP_COUNT_DELTA)
                .await(1, TimeUnit.MINUTES)

        result.total?.let { showDataSet(it) }
    }

    private fun showDataSet(dataSet: DataSet) {
        val dateFormat: DateFormat = DateFormat.getDateInstance()
        val timeFormat: DateFormat = DateFormat.getTimeInstance()
        for (dp in dataSet.dataPoints) {
            Log.d("History", "Data point:")
            Log.d("History", "\tType: " + dp.dataType.name)
            Log.d(
                "History",
                "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS))
                    .toString() + " " + timeFormat.format(
                    dp.getStartTime(
                        TimeUnit.MILLISECONDS
                    )
                )
            )
            Log.d(
                "History",
                "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS))
                    .toString() + " " + timeFormat.format(
                    dp.getStartTime(
                        TimeUnit.MILLISECONDS
                    )
                )
            )
            for (field in dp.dataType.fields) {
                Log.d(
                    "History", "\tField: " + field.name +
                            " Value: " + dp.getValue(field)
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
                displayStepDataForToday()
            }
        }
        else{

        }
    }
}