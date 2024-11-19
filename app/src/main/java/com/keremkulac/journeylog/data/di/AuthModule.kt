package com.keremkulac.journeylog.data.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.domain.repository.AuthRepository
import com.keremkulac.journeylog.domain.repository.AuthRepositoryImp
import com.keremkulac.journeylog.domain.repository.FirestoreRepository
import com.keremkulac.journeylog.domain.repository.FirestoreRepositoryImp
import com.keremkulac.journeylog.util.FirebaseException
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        googleSignInClient: GoogleSignInClient,
        context: Context,
        firebaseException: FirebaseException
    ): AuthRepository {
        return AuthRepositoryImp(firebaseAuth, googleSignInClient, context, firebaseException)
    }

    @Provides
    fun provideFirebaseException(): FirebaseException {
        return FirebaseException()
    }

    @Provides
    @Singleton
    fun provideGoogleSignInClient(
        @ApplicationContext context: Context
    ): GoogleSignInClient {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, googleSignInOptions)
    }

    @Provides
    fun provideReceiptRepository(
        firestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ): FirestoreRepository {
        return FirestoreRepositoryImp(firestore, firebaseStorage)
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

}