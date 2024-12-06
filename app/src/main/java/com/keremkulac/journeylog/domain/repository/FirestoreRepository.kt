package com.keremkulac.journeylog.domain.repository

import android.net.Uri
import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.domain.model.Vehicle
import com.keremkulac.journeylog.util.Result

interface FirestoreRepository {

    suspend fun saveReceipt(receipt: Receipt, result: (Result<String>) -> Unit)

    suspend fun getUser(id: String, result: (Result<Any>) -> Unit)

    suspend fun registerUser(user: User, result: (Result<String>) -> Unit)

    suspend fun saveProfilePicture(imageUri: Uri, path: String, result: (Result<String>) -> Unit)

    suspend fun updateUser(user: User, result: (Result<String>) -> Unit)

    suspend fun getProfilePictureUrl(path: String, result: (Result<String>) -> Unit)

    suspend fun getAllReceipts(result: (Result<Any>) -> Unit)

    suspend fun saveVehicle(vehicle: Vehicle, result: (Result<String>) -> Unit)
}