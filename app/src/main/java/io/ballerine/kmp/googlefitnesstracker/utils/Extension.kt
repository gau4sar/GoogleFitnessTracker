package io.ballerine.kmp.googlefitnesstracker.utils

import android.content.Context
import android.widget.Toast

fun Context.showToast(text:String){
    Toast.makeText(this,text, Toast.LENGTH_LONG).show()
}

fun Double.formatTo1Decimal():String{
    return String.format(
        "%.1f",
        this
    )
}