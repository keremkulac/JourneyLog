package com.keremkulac.journeylog.data.di

import android.content.Context
import androidx.room.Room
import com.keremkulac.journeylog.data.local.dao.AverageFuelPriceDao
import com.keremkulac.journeylog.data.local.database.AverageFuelPriceDatabase
import com.keremkulac.journeylog.data.repository.AverageFuelPrice
import com.keremkulac.journeylog.data.repository.AverageFuelPriceImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext appContext: Context): AverageFuelPriceDatabase {
        return Room.databaseBuilder(
            appContext,
            AverageFuelPriceDatabase::class.java,
            "averageFuelPriceDatabase.db"
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(averageFuelPriceDatabase: AverageFuelPriceDatabase): AverageFuelPriceDao {
        return averageFuelPriceDatabase.averageFuelPriceDao()
    }

    @Provides
    @Singleton
    fun provideMyRepository(averageFuelPriceDao: AverageFuelPriceDao): AverageFuelPrice {
        return AverageFuelPriceImp(averageFuelPriceDao)
    }
}