package com.keremkulac.journeylog.domain.usecase

import android.net.Uri
import com.keremkulac.journeylog.domain.repository.FirestoreRepository
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class SaveProfilePictureUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {
    suspend fun invoke(imageUri: Uri,path : String, result: (Result<String>) -> Unit) {
        firestoreRepository.saveProfilePicture(imageUri,path) {
            result.invoke(it)
        }
    }
}