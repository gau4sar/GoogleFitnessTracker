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
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.result.DataReadResponse
import io.ballerine.kmp.googlefitnesstracker.screens.GoogleSignInScreen
import io.ballerine.kmp.googlefitnesstracker.screens.HomeScreen
import io.ballerine.kmp.googlefitnesstracker.ui.theme.GoogleFitnessTrackerTheme
import io.ballerine.kmp.googlefitnesstracker.ui.theme.STATUS_BAR_COLOR
import io.ballerine.kmp.googlefitnesstracker.utils.Constant.AVERAGE
import io.ballerine.kmp.googlefitnesstracker.utils.SignOutDialog
import io.ballerine.kmp.googlefitnesstracker.utils.showToast
import java.text.DateFormat
import java.text.DateFormat.getTimeInstance
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {

    private val googleFitRequestCode = 1001

    private var stepsMutableState = mutableStateOf("")
    private var speedOfASessionMutableState = mutableStateOf("0")
    private var distanceOfASessionMutableState = mutableStateOf("0")

    private var isGoogleSignInProgress = mutableStateOf(false)

    private var isShowSignOutDialog = mutableStateOf(false)


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
                            isGoogleSignInProgress = isGoogleSignInProgress,
                            onGoogleSignInClick = {
                                isGoogleSignInProgress.value = true
                                setUpGoogleSignIn()
                            })
                    } else {
                        HomeScreen(
                            stepsMutableState = stepsMutableState,
                            speedOfASessionMutableState = speedOfASessionMutableState,
                            distanceOfASessionMutableState = distanceOfASessionMutableState,
                            onGoogleSignOutClick = {
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
            isGoogleSignInProgress.value = false
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

        /*  val readRequest = queryFitnessData()
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
          }*/


        GoogleSignIn.getLastSignedInAccount(this)?.let {

            val historyClient = Fitness.getHistoryClient(this, it)

            //Total steps in a day
            historyClient
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener { result ->
                    stepsMutableState.value =
                        (result.dataPoints.firstOrNull()?.getValue(Field.FIELD_STEPS)?.asInt()
                            ?: 0).toString()
                    // Do something with totalSteps
                }
                .addOnFailureListener { e ->
                    showToast("There was a problem reading the data. $e")
                    Log.e("tag", "There was a problem reading the data.", e)
                }

            //Average daily speed
            historyClient
                .readDailyTotal(DataType.TYPE_SPEED)
                .addOnSuccessListener { result ->
                    dumpDataSetForSpeed(result)
                }
                .addOnFailureListener { e ->
                    showToast("There was a problem reading the data. $e")
                    Log.e("tag", "There was a problem reading the data.", e)
                }

            //Total distance in a day
            historyClient
                .readDailyTotal(DataType.TYPE_DISTANCE_DELTA)
                .addOnSuccessListener { result ->
                    dumpDataSetForDistance(result)
                }
                .addOnFailureListener { e ->
                    showToast("There was a problem reading the data. $e")
                    Log.e("tag", "There was a problem reading the data.", e)
                }

            /*Fitness.getHistoryClient(this, it)
                .readData(readRequestSpeed)
                .addOnSuccessListener { dataReadResponse ->
                    printSpeedData(dataReadResponse)
                }
                .addOnFailureListener { e ->
                    showToast("There was a problem reading the data. $e")
                    Log.e("tag", "There was a problem reading the data.", e)
                }*/
        }
    }

    private fun dumpDataSetForSpeed(dataSet: DataSet) {
        Log.d("dumpDataSetForSpeed", "Data returned for Data type: " + dataSet.dataType.name)
        for (dp in dataSet.dataPoints) {
            val dateFormat: DateFormat = getTimeInstance()
            Log.d("dumpDataSetForSpeed", "Data point:")
            Log.d("dumpDataSetForSpeed", "\tType: " + dp.dataType.name)
            Log.d(
                "dumpDataSetForSpeed",
                "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS))
            )
            Log.d(
                "dumpDataSetForSpeed",
                "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS))
            )
            for (field in dp.dataType.fields) {
                Log.d(
                    "dumpDataSetForSpeed", "\tField: " + field.name +
                            " Value: " + dp.getValue(field)
                )

                if (field.name == AVERAGE) {
                    speedOfASessionMutableState.value = dp.getValue(field).toString()
                }
            }
        }
    }

    private fun dumpDataSetForDistance(dataSet: DataSet) {
        Log.d("dumpDataSetForDistance", "Data returned for Data type: " + dataSet.dataType.name)
        for (dp in dataSet.dataPoints) {
            val dateFormat: DateFormat = getTimeInstance()
            Log.d("dumpDataSetForDistance", "Data point:")
            Log.d("dumpDataSetForDistance", "\tType: " + dp.dataType.name)
            Log.d(
                "dumpDataSetForDistance",
                "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS))
            )
            Log.d(
                "dumpDataSetForDistance",
                "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS))
            )
            for (field in dp.dataType.fields) {
                Log.d(
                    "dumpDataSetForDistance", "\tField: " + field.name +
                            " Value: " + dp.getValue(field)
                )

                distanceOfASessionMutableState.value = dp.getValue(field).toString()
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