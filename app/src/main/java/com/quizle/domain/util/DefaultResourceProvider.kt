package com.quizle.domain.util

import android.content.Context


class DefaultResourceProvider(private val context: Context) : ResourceProvider {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }
}
