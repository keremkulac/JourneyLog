package com.keremkulac.journeylog.domain.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class FirestoreRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : FirestoreRepository {

    override suspend fun saveReceipt(receipt: Receipt, result: (Result<String>) -> Unit) {
        firestore.collection("receipts").add(receipt).addOnSuccessListener {
            result.invoke(Result.Success("Kayıt başarılı"))

        }.addOnFailureListener { exception ->
            result.invoke(Result.Failure(exception.message))
        }
    }

    override suspend fun registerUser(user: User, result: (Result<String>) -> Unit) {
        firestore.collection("users")
            .document(user.id)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    result.invoke(Result.Success("Kullanıcı zaten kayıtlı"))
                } else {
                    firestore.collection("users").document(user.id)
                        .set(user)
                        .addOnSuccessListener {
                            result.invoke(Result.Success("Kullanıcı başarıyla kaydedildi"))

                        }
                        .addOnFailureListener { e ->
                            result.invoke(Result.Failure("Kayıt yapılamadı"))
                        }
                }
            }
            .addOnFailureListener { e ->
                result.invoke(Result.Failure("Kayıt yapılamadı"))
            }
    }

    override suspend fun saveProfilePicture(
        imageUri: Uri,
        path: String,
        result: (Result<String>) -> Unit
    ) {
        firebaseStorage.getReference().child("images/${path}").putFile(imageUri)
            .addOnSuccessListener {
                result.invoke(Result.Success("Profil fotoğrafı başarıyla yüklendi"))
            }.addOnFailureListener {
                result.invoke(Result.Failure("Profil fotoğrafı yüklenirken hata oluştu"))
            }
    }

    override suspend fun updateUser(user: User, result: (Result<String>) -> Unit) {
        firestore.collection("users").document(user.id).set(user).addOnSuccessListener {
            result.invoke(Result.Success("Kullanıcı başarıyla güncellendi"))
        }.addOnFailureListener {
            result.invoke(Result.Failure("Kullanıcı güncellemesi başarısız"))
        }
    }

    override suspend fun getUser(id: String, result: (Result<Any>) -> Unit) {
        firestore.collection("users")
            .get()
            .addOnSuccessListener { task ->
                val user = task.toObjects(User::class.java)
                for (userObject in user) {
                    if (userObject.id == id) {
                        result.invoke(Result.Success(userObject))
                    }
                }
            }
            .addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.message))
            }
    }

}