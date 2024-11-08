package com.keremkulac.journeylog.data.di

import com.google.firebase.auth.FirebaseAuth
import com.keremkulac.journeylog.domain.repository.AuthRepository
import com.keremkulac.journeylog.domain.repository.AuthRepositoryImp
import com.keremkulac.journeylog.domain.usecase.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository{
        return AuthRepositoryImp(firebaseAuth)
    }

    @Provides
    fun provideRegisterUseCase(authRepositoryImp: AuthRepositoryImp) = RegisterUseCase(authRepositoryImp)

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

}