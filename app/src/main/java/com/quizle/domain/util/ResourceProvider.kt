package com.quizle.domain.util


interface ResourceProvider {
    fun getString(resId: Int): String
}
