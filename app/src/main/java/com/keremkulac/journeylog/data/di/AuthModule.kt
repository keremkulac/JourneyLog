package com.keremkulac.journeylog.data.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.domain.repository.AuthRepository
import com.keremkulac.journeylog.domain.repository.AuthRepositoryImp
import com.keremkulac.journeylog.domain.repository.ReceiptRepository
import com.keremkulac.journeylog.domain.repository.ReceiptRepositoryImp
import com.keremkulac.journeylog.domain.usecase.RegisterUseCase
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
    fun provideAuthRepository(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore,googleSignInClient: GoogleSignInClient,context: Context): AuthRepository {
        return AuthRepositoryImp(firebaseAuth,firestore,googleSignInClient,context)
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
    fun provideReceiptRepository(firestore: FirebaseFirestore): ReceiptRepository {
        return ReceiptRepositoryImp(firestore)
    }

    @Provides
    fun provideRegisterUseCase(authRepositoryImp: AuthRepositoryImp) =
        RegisterUseCase(authRepositoryImp)

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