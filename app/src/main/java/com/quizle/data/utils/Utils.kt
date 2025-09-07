package com.quizle.data.utils


fun Int.toKb(): Int = (this / 1024)


sealed class IssueType(val key: String) {
    data object IncorrectAnswer : IssueType("incorrect_answer")
    data object QuestionUnclear : IssueType("unclear_question")
    data object TypoGrammarMistake : IssueType("typo_grammar_mistake")
    data class Other(val otherDescription: String) : IssueType(otherDescription) {
    }

    companion object {
        fun fromKey(key: String): IssueType {
            return when (key) {
                "incorrect_answer" -> IncorrectAnswer
                "unclear_question" -> QuestionUnclear
                "typo_grammar_mistake" -> TypoGrammarMistake
                else -> Other(key) // If the key doesn't match, assume it's a custom "Other" type
            }
        }
    }
}


sealed class Gender(val name: String) {
    data object Male : Gender("male")
    data object Female: Gender("female")

    companion object {
        fun fromKey(key: String): Gender {
            return if (key == "male") Male else Female
        }
    }
}

