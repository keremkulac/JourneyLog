package com.keremkulac.journeylog.domain.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class ReceiptRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore
) : ReceiptRepository {

    override suspend fun saveReceipt(receipt: Receipt, result: (Result<String>) -> Unit) {
        firestore.collection("receipt").add(receipt).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                result.invoke(Result.Success("Kayıt başarılı"))
            } else {
                result.invoke(Result.Failure("Kayıt başarısız"))
            }
        }
    }

}