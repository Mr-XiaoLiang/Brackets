package com.lollipop.brackets.core

import android.content.res.Resources
import android.util.TypedValue

inline val <reified T : Number> T.dp: Float
    get() {
        val float = toFloat()
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            float,
            Resources.getSystem().displayMetrics
        )
    }

inline val <reified T : Number> T.sp: Float
    get() {
        val float = toFloat()
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            float,
            Resources.getSystem().displayMetrics
        )
    }