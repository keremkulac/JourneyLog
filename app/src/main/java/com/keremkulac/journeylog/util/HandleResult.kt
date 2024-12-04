package com.keremkulac.journeylog.util

import android.view.View

object HandleResult {
    fun <T> handleResult(
        progressBar: View?,
        result: Result<T>,
        onSuccess: (T) -> Unit,
        onFailure: ((String?) -> Unit)? = null
    ) {
        when (result) {
            Result.Loading -> progressBar?.visibility = View.VISIBLE
            is Result.Failure -> {
                progressBar?.visibility = View.GONE
                onFailure?.invoke(result.error)
            }

            is Result.Success -> {
                progressBar?.visibility = View.GONE
                onSuccess(result.data)
            }
        }
    }
}