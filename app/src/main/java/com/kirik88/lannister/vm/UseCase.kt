package com.kirik88.lannister.vm

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData

data class UseCase<T>(
    var inProgress: Boolean = false,
    var error: Exception? = null,
    var result: T? = null
)

class LiveUseCase<T> : MutableLiveData<UseCase<T>>()

inline fun <T> LiveUseCase<T>.execute(block: () -> T) {
    value = UseCase(inProgress = true)
    value = try {
        val result = block()
        UseCase(result = result)
    } catch (error: Exception) {
        Log.e("UseCase", error.message, error)
        UseCase(error = error)
    }
}

fun <T> UseCase<T>?.consume(context: Context) {
    consume(context) {}
}

inline fun <T> UseCase<T>?.consume(context: Context, block: (T) -> Unit) {
    onResult {
        block(it)
        this!!.result = null
    }

    onError {
        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        this!!.error = null
    }
}

inline fun <T> UseCase<T>?.onResult(block: (T) -> Unit) {
    this?.result?.let {
        block(it)
    }
}

inline fun UseCase<*>?.onError(block: (Exception) -> Unit) {
    this?.error?.let {
        block(it)
    }
}