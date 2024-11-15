package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.domain.repository.FirestoreRepositoryImp
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class SaveReceiptUseCase @Inject constructor(private val receiptRepositoryImp: FirestoreRepositoryImp) {
    suspend fun invoke(receipt: Receipt, result: (Result<String>) -> Unit) {
        receiptRepositoryImp.saveReceipt(receipt) {
            result(it)
        }
    }
}