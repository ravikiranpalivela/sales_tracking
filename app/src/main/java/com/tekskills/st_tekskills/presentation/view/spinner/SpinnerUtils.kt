package com.tekskills.st_tekskills.presentation.view.spinner

import android.content.Context

object SpinnerUtils {

    fun dpToPx(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }
}
