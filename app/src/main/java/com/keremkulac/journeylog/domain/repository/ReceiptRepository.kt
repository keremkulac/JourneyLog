package com.keremkulac.journeylog.domain.repository

import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.util.Result

interface ReceiptRepository {

    suspend fun saveReceipt(receipt: Receipt, result: (Result<String>) -> Unit)

}