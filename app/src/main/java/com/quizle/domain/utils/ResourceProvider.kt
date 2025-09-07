package com.quizle.domain.utils


interface ResourceProvider {
    fun getString(resId: Int): String
}
