package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.domain.repository.AuthRepositoryImp
import javax.inject.Inject

class SaveReceiptUseCase @Inject constructor(private val authRepositoryImp: AuthRepositoryImp) {
    suspend fun invoke(receipt: Receipt) = authRepositoryImp.saveReceipt(receipt)
}