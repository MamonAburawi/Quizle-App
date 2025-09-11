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
import androidx.compose.ui.graphics.Color.Companion.White
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
import com.quizle.presentation.common.LoadingButton
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.theme.Blue
import com.quizle.presentation.theme.DarkBackground
import com.quizle.presentation.theme.Gray
import com.quizle.presentation.theme.SurfaceColor
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
    onFullNameChanged:(String) -> Unit,
    onGenderChanged:(Gender) -> Unit,
    onSaveButtonClicked:() -> Unit,
    onImageProfile:(Uri) -> Unit,
    onDeleteImageProfile: (String) -> Unit
) {

    val user = state.user
    val imageProfile = user.imageProfile
    val fullName = user.userName
    val gender = user.gender ?: Gender.Male.name


    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->

//        imageUri = uri
        if (uri != null){
            onImageProfile(uri)
        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- HEADER ---
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = stringResource(R.string.edit_profile),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = White
        )
        Spacer(modifier = Modifier.height(40.dp))

        // --- PROFILE IMAGE ---
        ProfileImage(
            imageUri = imageProfile,
            onImageClick = { imagePickerLauncher.launch("image/*") },
            isLoading = state.isImageProfileLoading,
            onDeleteImageProfile = {onDeleteImageProfile(it)}
        )

        Spacer(modifier = Modifier.height(40.dp))


        OutlinedTextField(
            value = fullName,
            onValueChange = { 
                onFullNameChanged(it)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.full_name), color = Gray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Full Name Icon",
                    tint = Gray
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White,
                unfocusedTextColor = Gray,
                cursorColor = Blue,
                focusedBorderColor = Blue,
                unfocusedBorderColor = SurfaceColor,
                focusedLabelColor = Blue,
                unfocusedLabelColor = Gray,
                focusedLeadingIconColor = Blue,
                unfocusedLeadingIconColor = Gray
            )
        )
        Spacer(modifier = Modifier.height(24.dp))

        // --- GENDER SWITCH ---
        GenderSwitch(
            selectedGender = Gender.fromKey(gender),
            onGenderSelected = { onGenderChanged(it) }
        )

        Spacer(modifier = Modifier.weight(1f)) // Pushes the button to the bottom

        LoadingButton(
            onClick = { onSaveButtonClicked() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            text = stringResource(R.string.save_changes),
            color = Blue,
            loadingColor = Blue,
            isLoading = state.isLoading
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
                .background(SurfaceColor)
                .clickable { onImageClick() }
                .border(2.dp, Blue, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Use AsyncImage for efficient, asynchronous image loading
            AsyncImage(
                model = imageUri,
                contentDescription = "Profile Picture",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri == null){
                    Icon(
                        painter = painterResource(R.drawable.ic_camera),
                        contentDescription = "Edit Image",
                        tint = White.copy(alpha = 0.8f),
                        modifier = Modifier.size(32.dp)
                    )
                }

                if(isLoading){
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = White,
                        strokeWidth = 2.dp
                    )
                }

            }


        }

        if (imageUri != null){
            LoadingButton(
                onClick = { onDeleteImageProfile(imageUri) },
                modifier = Modifier
                    .height(45.dp)
                    .width(120.dp)
                    .padding(top = 10.dp),
                text = "Delete",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                color = MaterialTheme.colorScheme.error,
                loadingColor = MaterialTheme.colorScheme.error,
            )
        }

    }

}

@Composable
fun GenderSwitch(selectedGender: Gender, onGenderSelected: (Gender) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Gender", color = Gray, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceColor)
        ) {
            val maleColor by animateColorAsState(
                targetValue = if (selectedGender == Gender.Male) Blue else Color.Transparent,
                label = "MaleColorAnimation"
            )
            val femaleColor by animateColorAsState(
                targetValue = if (selectedGender == Gender.Female) Blue else Color.Transparent,
                label = "FemaleColorAnimation"
            )

            // --- MALE OPTION ---
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                    .background(maleColor)
                    .clickable { onGenderSelected(Gender.Male) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.male),
                    color = White,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }

            // --- FEMALE OPTION ---
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                    .background(femaleColor)
                    .clickable { onGenderSelected(Gender.Female) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.female),
                    color = White,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


//@Composable
//fun ProfileContent() {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(DarkBackground),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//
//        Text(
//            text = "Profile",
//            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
//            fontWeight = FontWeight.Bold,
//            color = Color.White
//        )
//
//    }
//}

@Preview(showBackground = true)
@Composable
private fun ProfilePreview() {
    val state = ProfileState(
        isLoading = true
    )
    ProfileContent(
        state = state,
        onFullNameChanged = {},
        onGenderChanged = {},
        onSaveButtonClicked = {},
        onImageProfile = {},
        onDeleteImageProfile = {}
    )
}
