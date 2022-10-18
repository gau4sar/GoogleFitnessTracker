package io.ballerine.kmp.googlefitnesstracker.data

data class DailySteps(var day: String, var steps: Float) {

    @JvmName("getSteps1")
    fun getSteps(): Float {
        return if (steps > 1f) 1f else steps
    }
}
