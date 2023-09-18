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

object BracketsUtils  {
    inline fun <reified B : Brackets<*>, reified P : Protocol> createBuilder(
        scope: Scope,
        builder: P.() -> Unit
    ) {
        val protocol = P::class.java.newInstance()
        builder(protocol)
        val constructor = B::class.java.getConstructor(P::class.java)
        val brackets = constructor.newInstance(protocol)
        scope.add(brackets)
    }
}
