package com.keremkulac.journeylog.domain.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class ReceiptRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore
) : ReceiptRepository {

    override suspend fun saveReceipt(receipt: Receipt, result: (Result<String>) -> Unit) {
        firestore.collection("receipts").add(receipt).addOnSuccessListener {
            result.invoke(Result.Success("Kayıt başarılı"))

        }.addOnFailureListener { exception ->
            result.invoke(Result.Failure(exception.message))
        }
    }

}