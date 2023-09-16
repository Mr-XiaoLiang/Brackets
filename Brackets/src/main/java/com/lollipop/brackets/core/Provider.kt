package com.lollipop.brackets.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.reflect.KProperty

fun interface TypedProvider<T> {
    operator fun invoke(): T
}

fun interface TypedResponse<T> {
    operator fun invoke(any: T)
}

class Stateless<T>(private val value: T) : TypedProvider<T> {
    override operator fun invoke(): T {
        return value
    }
}

class Stateful<T>(private val defValue: T) : TypedProvider<T> {

    private val liveData = MutableLiveData(defValue)

    val live: LiveData<T>
        get() {
            return liveData
        }

    fun set(newValue: T) {
        liveData.value = newValue
    }

    override operator fun invoke(): T {
        return liveData.value ?: defValue
    }

    operator fun getValue(target: Any, property: KProperty<*>): T {
        return invoke()
    }

    operator fun setValue(target: Any, property: KProperty<*>, newValue: T) {
        set(newValue)
    }
}

fun <T> remember(defValue: T) = Stateful(defValue)
