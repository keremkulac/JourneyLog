package com.keremkulac.journeylog.domain.repository

import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.util.Result

interface FirestoreRepository {

    suspend fun saveReceipt(receipt: Receipt, result: (Result<String>) -> Unit)

    suspend fun getUser(id: String, result: (Result<Any>) -> Unit)

    suspend fun registerUser(user: User, result: (Result<String>) -> Unit)

}