package com.quizle.presentation.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.quizle.R
import com.quizle.data.utils.Gender
import com.quizle.domain.module.User
import com.quizle.presentation.common.LoadingButton
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.theme.QuizleTheme
import com.quizle.presentation.theme.extendedColors
import com.quizle.presentation.theme.unSelected
import com.quizle.presentation.util.relaunchApp
import kotlinx.coroutines.flow.Flow


@Composable
fun ProfileScreen(
    toastManager: ToastMessageController,
    state: ProfileState,
    event: Flow<ProfileEvent>,
    onAction: (ProfileAction) -> Unit
) {
    val context = LocalContext.current
    ProfileContent(
        state = state,
        onFullNameChanged = {
            onAction(ProfileAction.OnFullNameTextChanged(it))
        },
        onGenderChanged = {
            onAction(ProfileAction.GenderButtonClicked(it.name))
        },
        onSaveButtonClicked = {
            onAction(ProfileAction.SaveChangeButtonClicked)
        },
        onImageProfile = { uri ->
            onAction(ProfileAction.UploadImageButtonClicked(uri))
        },
        onDeleteImageProfile = {
            onAction(ProfileAction.DeleteImageProfileButtonClicked(it))
        }
    )

    LaunchedEffect(
        key1 = Unit
    ) {
        event.collect {
            when(it){
                is ProfileEvent.NavigateToHomeScreen -> {
                    context.relaunchApp()
                }
                is ProfileEvent.ShowMessage -> {
                    toastManager.showToast(it.message, it.type)
                }
            }
        }
    }

}

@Composable
fun ProfileContent(
    state: ProfileState,
    onFullNameChanged: (String) -> Unit,
    onGenderChanged: (Gender) -> Unit,
    onSaveButtonClicked: () -> Unit,
    onImageProfile: (Uri) -> Unit,
    onDeleteImageProfile: (String) -> Unit
) {
    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { onImageProfile(it) }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColors.backgroundColor)
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = stringResource(R.string.edit_profile),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            // NEW: Theme-aware color
            color = MaterialTheme.extendedColors.onBackgroundColor
        )
        Spacer(modifier = Modifier.height(40.dp))

        ProfileImage(
            isLoading = state.isImageProfileLoading,
            imageUri = state.user.imageProfile,
            onImageClick = { imagePickerLauncher.launch("image/*") },
            onDeleteImageProfile = { onDeleteImageProfile(it) }
        )
        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = state.user.userName,
            onValueChange = onFullNameChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.full_name)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Full Name Icon"
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
//                focusedBorderColor = MaterialTheme.extendedColors.secondaryColor,
//                unfocusedBorderColor = MaterialTheme.extendedColors.secondaryColor,
//                cursorColor = MaterialTheme.extendedColors.secondaryColor
            )
        )
        Spacer(modifier = Modifier.height(30.dp))

        GenderSwitch(
            selectedGender = Gender.fromKey(state.user.gender ?: Gender.Male.name),
            onGenderSelected = onGenderChanged
        )
        Spacer(modifier = Modifier.weight(1f))

        // NEW: Themed LoadingButton
        LoadingButton(
            onClick = onSaveButtonClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            text = stringResource(R.string.save_changes),
            isLoading = state.isLoading,
            bgColor = MaterialTheme.extendedColors.surfaceColor,
            contentColor = MaterialTheme.extendedColors.onSurfaceColor
        )
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun ProfileImage(
    isLoading: Boolean,
    imageUri: String?,
    onImageClick: () -> Unit,
    onDeleteImageProfile: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
                .background(if (imageUri == null) Color.unSelected else Color.Unspecified)
                .border(2.dp, MaterialTheme.extendedColors.secondaryColor, CircleShape)
                .clickable(onClick = onImageClick),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = imageUri ?: R.drawable.ic_camera,
                contentDescription = "Profile Picture",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = if (imageUri != null) 1f else 0.3f // Make placeholder icon less prominent
            )
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = MaterialTheme.extendedColors.onBackgroundColor,
                    strokeWidth = 2.dp
                )
            }
        }
        if (imageUri != null && !isLoading) {
            TextButton(onClick = { onDeleteImageProfile(imageUri) }) {
                Text(
                    text = "Delete",
                    color = MaterialTheme.extendedColors.error
                )
            }
        }
    }
}

@Composable
fun GenderSwitch(selectedGender: Gender, onGenderSelected: (Gender) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Gender",
            // NEW: Theme-aware color
            color = MaterialTheme.extendedColors.onSurfaceColor,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.extendedColors.onSurfaceColor)
        ) {
            // Animate background color
            val maleBgColor by animateColorAsState(
                if (selectedGender == Gender.Male) MaterialTheme.extendedColors.primaryColor else Color.Transparent, label = ""
            )
            val femaleBgColor by animateColorAsState(
                if (selectedGender == Gender.Female) MaterialTheme.extendedColors.primaryColor else Color.Transparent, label = ""
            )
            // Animate text color for contrast
            val maleTextColor by animateColorAsState(
                if (selectedGender == Gender.Male) MaterialTheme.extendedColors.textPrimaryColor else MaterialTheme.extendedColors.surfaceColor, label = ""
            )
            val femaleTextColor by animateColorAsState(
                if (selectedGender == Gender.Female) MaterialTheme.extendedColors.textPrimaryColor else MaterialTheme.extendedColors.surfaceColor, label = ""
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(maleBgColor)
                    .clickable { onGenderSelected(Gender.Male) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.male),
                    color = maleTextColor,
                    fontWeight = FontWeight.Medium
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                    .background(femaleBgColor)
                    .clickable { onGenderSelected(Gender.Female) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.female),
                    color = femaleTextColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}



@Preview(name = "Profile - Light Theme")
@Composable
private fun ProfileLightPreview() {
    QuizleTheme(darkTheme = false) {
        val state = ProfileState(user = User(userName = "Mamon Aburawi", gender = Gender.Male.name))
        ProfileContent(state, {}, {}, {}, {}, {})
    }
}

@Preview(name = "Profile - Dark Theme")
@Composable
private fun ProfileDarkPreview() {
    QuizleTheme(darkTheme = true) {
        val state = ProfileState(user = User(userName = "Mamon Aburawi", imageProfile = "dummy_url", gender = Gender.Female.name))
        ProfileContent(state, {}, {}, {}, {}, {})
    }
}
