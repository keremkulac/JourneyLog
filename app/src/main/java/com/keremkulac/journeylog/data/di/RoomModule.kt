package com.keremkulac.journeylog.data.di

import android.content.Context
import androidx.room.Room
import com.keremkulac.journeylog.data.local.dao.AverageFuelPriceDao
import com.keremkulac.journeylog.data.local.dao.CompanyDao
import com.keremkulac.journeylog.data.local.database.AverageFuelPriceDatabase
import com.keremkulac.journeylog.data.repository.AverageFuelPriceRepository
import com.keremkulac.journeylog.data.repository.AverageFuelPriceRepositoryImp
import com.keremkulac.journeylog.util.LastUpdateSharedPreferences
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
    fun provideCompanyDao(averageFuelPriceDatabase: AverageFuelPriceDatabase): CompanyDao {
        return averageFuelPriceDatabase.companyDao()
    }

    @Provides
    @Singleton
    fun provideMyRepository(averageFuelPriceDao: AverageFuelPriceDao): AverageFuelPriceRepository {
        return AverageFuelPriceRepositoryImp(averageFuelPriceDao)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext appContext: Context): LastUpdateSharedPreferences {
        return LastUpdateSharedPreferences(appContext)
    }
}