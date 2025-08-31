package com.quizle.presentation.screens.profile

import android.net.Uri

sealed interface ProfileAction {

    data object SaveChangeButtonClicked: ProfileAction
    data class GenderButtonClicked(val gender: String): ProfileAction

    data class OnFullNameTextChanged(val fullName: String): ProfileAction

    data class UploadImageButtonClicked(val uri: Uri): ProfileAction

    data class DeleteImageProfileButtonClicked(val imageUrl: String): ProfileAction

}