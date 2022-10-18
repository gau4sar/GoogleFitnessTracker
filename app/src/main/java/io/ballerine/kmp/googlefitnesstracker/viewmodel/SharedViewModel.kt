package io.ballerine.kmp.googlefitnesstracker.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.fitness.HistoryClient
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.result.DataReadResponse
import io.ballerine.kmp.googlefitnesstracker.data.DailySteps
import io.ballerine.kmp.googlefitnesstracker.utils.Constant
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class SharedViewModel : ViewModel() {

    var stepsMutableState = mutableStateOf("0")
    var speedOfASessionMutableState = mutableStateOf("0")
    var distanceOfASessionMutableState = mutableStateOf("0")

    var isGoogleSignInProgress = mutableStateOf(false)
    var isShowSignOutDialog = mutableStateOf(false)

    var dailySteps = mutableStateListOf<DailySteps>()
    var errorMessages = mutableStateListOf<String>()

    fun readRequests(historyClient: HistoryClient) {
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
                errorMessages.add("There was a problem reading the data. $e")
                Log.e("tag", "There was a problem reading the data.", e)
            }

        //Average daily speed
        historyClient
            .readDailyTotal(DataType.TYPE_SPEED)
            .addOnSuccessListener { result ->
                dumpDataSetForSpeed(result)
            }
            .addOnFailureListener { e ->
                errorMessages.add("There was a problem reading the data. $e")
                Log.e("tag", "There was a problem reading the data.", e)
            }

        //Total distance in a day
        historyClient
            .readDailyTotal(DataType.TYPE_DISTANCE_DELTA)
            .addOnSuccessListener { result ->
                dumpDataSetForDistance(result)
            }
            .addOnFailureListener { e ->
                errorMessages.add("There was a problem reading the data. $e")
                Log.e("tag", "There was a problem reading the data.", e)
            }

        val cal = Calendar.getInstance()
        cal.time = Date()
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        val endTime = cal.timeInMillis

        cal.add(Calendar.WEEK_OF_MONTH, -1)
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        val startTime = cal.timeInMillis

        val estimatedStepsDelta: DataSource = DataSource.Builder()
            .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .setType(DataSource.TYPE_DERIVED)
            .setStreamName("estimated_steps")
            .setAppPackageName("com.google.android.gms")
            .build()

        val readRequest: DataReadRequest = DataReadRequest.Builder()
            .aggregate(estimatedStepsDelta, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .bucketByTime(1, TimeUnit.DAYS)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .build()

        historyClient
            .readData(readRequest)
            .addOnSuccessListener { dataReadResponse ->
                printData(dataReadResponse)
            }
            .addOnFailureListener { e ->
                errorMessages.add("There was a problem reading the data. $e")
                Log.e("tag", "There was a problem reading the data.", e)
            }
    }

    private fun dumpDataSetForSpeed(dataSet: DataSet) {
        Log.d("dumpDataSetForSpeed", "Data returned for Data type: " + dataSet.dataType.name)
        for (dp in dataSet.dataPoints) {
            val dateFormat: DateFormat = DateFormat.getTimeInstance()
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

                if (field.name == Constant.AVERAGE) {
                    speedOfASessionMutableState.value = dp.getValue(field).toString()
                }
            }
        }
    }

    private fun dumpDataSetForDistance(dataSet: DataSet) {
        Log.d("dumpDataSetForDistance", "Data returned for Data type: " + dataSet.dataType.name)
        for (dp in dataSet.dataPoints) {
            val dateFormat: DateFormat = DateFormat.getTimeInstance()
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

    /*private fun queryFitnessData(): DataReadRequest {
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
    }*/

    private fun printData(dataReadResult: DataReadResponse) {
        if (dataReadResult.buckets.size > 0) {
            for (bucket in dataReadResult.buckets) {
                val dataSets = bucket.dataSets
                for (dataSet in dataSets) {
                    dumpDataSetForWeeklySteps(dataSet)
                }
            }
        } else if (dataReadResult.dataSets.size > 0) {
            for (dataSet in dataReadResult.dataSets) {
                dumpDataSetForWeeklySteps(dataSet)
            }
        }
    }

    private fun dumpDataSetForWeeklySteps(dataSet: DataSet) {
        for (dp in dataSet.dataPoints) {
            val dateFormat: DateFormat = DateFormat.getTimeInstance()
            Log.d("dumpDataSetForDailySteps", "Data point:")
            Log.d("dumpDataSetForDailySteps", "\tType: " + dp.dataType.name)

            val formatter = SimpleDateFormat("EE", Locale.getDefault())
            val day = formatter.format(Date(dp.getTimestamp(TimeUnit.MILLISECONDS)))

            dailySteps.add(DailySteps(day, 0f))

            Log.d(
                "dumpDataSetForDailySteps",
                "\tStart: " + dp.getTimestamp(TimeUnit.DAYS) + " $day"
            )
            Log.d(
                "dumpDataSetForDailySteps",
                "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS))
            )
            for (field in dp.dataType.fields) {

                /*stepsMutableState.value = dp.getValue(field).toString()*/
                Log.d(
                    "dumpDataSetForDailySteps", "\tField: " + field.name +
                            " Value: " + dp.getValue(field)
                )

                dailySteps.find { it.day == day }?.steps =
                    dp.getValue(field).toString().toFloat() / Constant.GOAL_STEPS
            }
        }
    }

    fun clearDate() {
        dailySteps.clear()
        distanceOfASessionMutableState.value = "0"
        speedOfASessionMutableState.value = "0"
        stepsMutableState.value = "0"
    }

}