package com.keremkulac.journeylog.domain.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.keremkulac.journeylog.domain.model.AverageFuelPrice
import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.domain.model.Vehicle
import com.keremkulac.journeylog.util.FirebaseException
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class FirestoreRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseException: FirebaseException
) : FirestoreRepository {

    override suspend fun saveReceipt(receipt: Receipt, result: (Result<String>) -> Unit) {
        firestore.collection("receipts").add(receipt).addOnSuccessListener {
            result.invoke(Result.Success("Kayıt başarılı"))

        }.addOnFailureListener { exception ->
            result.invoke(Result.Failure(firebaseException.findExceptionMessage(exception)))
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
                        .addOnFailureListener {
                            result.invoke(Result.Failure("Kayıt yapılamadı"))
                        }
                }
            }
            .addOnFailureListener {
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

    override suspend fun getProfilePictureUrl(
        path: String,
        result: (Result<String>) -> Unit
    ) {
        firebaseStorage.getReference("images/${path}").downloadUrl
            .addOnSuccessListener { uri ->
                result.invoke(Result.Success(uri.toString()))
            }
            .addOnFailureListener { exception ->
                result.invoke(Result.Failure("Resim URL'si alınamadı: ${exception.localizedMessage}"))
            }
    }

    override suspend fun getAllReceipts(email: String, result: (Result<Any>) -> Unit) {
        firestore.collection("receipts").get()
            .addOnSuccessListener { snapshot ->
                val receipt = snapshot.toObjects(Receipt::class.java)
                val list = mutableListOf<Receipt>()
                for (receiptObject in receipt) {
                    if (receiptObject.email == email) {
                        list.add(receiptObject)
                    }
                }
                list.sortByDescending { it.date }
                result.invoke(Result.Success(list))

            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure("Veri çekme başarısız"))
            }
    }

    override suspend fun saveVehicle(vehicle: Vehicle, result: (Result<String>) -> Unit) {
        firestore.collection("vehicles").document(vehicle.id)
            .set(vehicle)
            .addOnSuccessListener {
                result.invoke(Result.Success("Araç kaydı başarılı"))

            }
            .addOnFailureListener { exception ->
                result.invoke(Result.Failure(firebaseException.findExceptionMessage(exception)))
            }

    }

    override suspend fun updateVehicle(vehicle: Vehicle, result: (Result<String>) -> Unit) {
        firestore.collection("vehicles").document(vehicle.id).set(vehicle).addOnSuccessListener {
            result.invoke(Result.Success("Araç başarıyla güncellendi"))
        }.addOnFailureListener {
            result.invoke(Result.Failure("Araç güncellemesi başarısız"))
        }
    }

    override suspend fun getAllVehicles(userId: String, result: (Result<Any>) -> Unit) {
        firestore.collection("vehicles").get().addOnSuccessListener { snapshot ->
            val vehicle = snapshot.toObjects(Vehicle::class.java)
            val list = mutableListOf<Vehicle>()
            for (vehicleObject in vehicle) {
                if (vehicleObject.userId == userId) {
                    list.add(vehicleObject)
                }
            }
            result.invoke(Result.Success(list))
        }.addOnFailureListener { exception ->
            result.invoke(Result.Failure(firebaseException.findExceptionMessage(exception)))
        }
    }

    override suspend fun getAverageFuelPrice(result: (Result<Any>) -> Unit) {
        firestore.collection("averageFuelPrice").get().addOnSuccessListener { snapshot ->
            val averageFuelPriceList = snapshot.toObjects(AverageFuelPrice::class.java)
            val list = mutableListOf<AverageFuelPrice>()
            for (averageFuelPrice in averageFuelPriceList) {
                list.add(averageFuelPrice)
            }
            result.invoke(Result.Success(list))
        }.addOnFailureListener { exception ->
            result.invoke(Result.Failure(firebaseException.findExceptionMessage(exception)))
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
                result.invoke(Result.Failure(firebaseException.findExceptionMessage(exception)))
            }
    }


}